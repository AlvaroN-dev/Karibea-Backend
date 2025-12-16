import subprocess
import sys
import os
import time
import platform
import shutil
import socket

# Global variable to store the detected compose command
COMPOSE_CMD = None

# Get the directory where this script is located
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))

def get_compose_file_path(environment):
    """Get the full path to the docker-compose file for the given environment."""
    return os.path.join(SCRIPT_DIR, f"docker-compose.{environment}.yml")

def check_docker_permissions():
    """Check if docker is accessible."""
    try:
        subprocess.run(
            ["docker", "info"], 
            stdout=subprocess.DEVNULL, 
            stderr=subprocess.DEVNULL, 
            check=True
        )
        return True
    except (subprocess.CalledProcessError, FileNotFoundError):
        return False

def get_compose_command():
    """Determine if we should use 'docker compose' or 'docker-compose'."""
    # Try 'docker compose' (V2)
    try:
        subprocess.run(
            ["docker", "compose", "version"], 
            stdout=subprocess.DEVNULL, 
            stderr=subprocess.DEVNULL, 
            check=True
        )
        return ["docker", "compose"]
    except (subprocess.CalledProcessError, FileNotFoundError):
        pass

    # Try 'docker-compose' (V1)
    if shutil.which("docker-compose"):
        return ["docker-compose"]
    
    return None

def initialize():
    """Initialize the environment, checking permissions and dependencies."""
    global COMPOSE_CMD
    
    # Check Docker accessibility
    if not check_docker_permissions():
        if platform.system() == "Linux":
            print("⚠️  Permission denied connecting to Docker daemon.")
            if os.geteuid() != 0:
                print("🔄 Attempting to restart with sudo...")
                try:
                    args = ["sudo", sys.executable] + sys.argv
                    os.execvp("sudo", args)
                except Exception as e:
                    print(f"❌ Failed to restart with sudo: {e}")
                    sys.exit(1)
            else:
                print("❌ Running as root but Docker is not responding.")
                print("💡 Check if Docker daemon is running (systemctl status docker)")
                sys.exit(1)
        else:
            print("❌ Docker is not running or not accessible.")
            print("💡 Please start Docker Desktop.")
            sys.exit(1)

    # Detect Compose command
    COMPOSE_CMD = get_compose_command()
    if not COMPOSE_CMD:
        print("❌ Docker Compose not found.")
        print("💡 Please install Docker Compose (v1 or v2).")
        sys.exit(1)

def is_container_running(container_name):
    """Check if a container is running and healthy."""
    try:
        result = subprocess.run(
            ["docker", "inspect", "-f", "{{.State.Running}}", container_name],
            capture_output=True, text=True, check=False
        )
        return result.stdout.strip() == "true"
    except Exception:
        return False

def is_container_healthy(container_name):
    """Check if a container is healthy."""
    try:
        result = subprocess.run(
            ["docker", "inspect", "-f", "{{.State.Health.Status}}", container_name],
            capture_output=True, text=True, check=False
        )
        return result.stdout.strip() == "healthy"
    except Exception:
        return False

def wait_for_healthy(container_name, timeout=120, interval=5):
    """Wait for a container to become healthy."""
    elapsed = 0
    while elapsed < timeout:
        if is_container_healthy(container_name):
            return True
        print(f"   ⏳ Waiting for {container_name} to be healthy... ({elapsed}s)")
        time.sleep(interval)
        elapsed += interval
    return False

def check_and_free_port_5432():
    """Check if port 5432 is in use and try to free it if it's a local service."""
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    result = sock.connect_ex(('127.0.0.1', 5432))
    sock.close()
    
    if result == 0: # Port is open (in use)
        print("⚠️  Port 5432 is already in use.")
        
        # Check if it's our postgres container
        if is_container_running("karibea-postgres"):
            print("   ℹ️  It seems to be the Docker container 'karibea-postgres'. Proceeding...")
            return True
            
        if platform.system() == "Linux":
            print("   🔄 Attempting to stop local PostgreSQL service...")
            try:
                subprocess.run(["sudo", "systemctl", "stop", "postgresql"], check=True)
                print("   ✅ Local PostgreSQL service stopped.")
                time.sleep(2) # Wait for it to release port
                return True
            except subprocess.CalledProcessError:
                print("   ❌ Failed to stop local PostgreSQL service.")
                print("   💡 Please stop it manually: sudo systemctl stop postgresql")
                return False
        else:
             print("   ❌ Port 5432 is in use. Please stop the local PostgreSQL service.")
             return False
    return True

def run_compose(environment):
    if environment not in ['dev', 'prod']:
        print("Invalid environment. Please choose 'dev' or 'prod'.")
        return
    
    compose_file = get_compose_file_path(environment)
    
    if not os.path.exists(compose_file):
        print(f"Error: {compose_file} not found.")
        return
    
    print(f"\n🚀 Starting Docker Compose for {environment.upper()} environment...")
    print("="*60)
    
    # Check for port conflicts
    if not check_and_free_port_5432():
        return
    
    # Define service groups in order of dependency
    database_services = ["postgres"]
    kafka_services = ["kafka-0", "kafka-1", "kafka-2"]
    kafka_init_service = ["kafka-init"]
    config_service = ["microservice-config"]
    eureka_service = ["microservice-eureka"]
    
    other_services = [
        "microservice-catalog", "microservice-chatbot",
        "microservice-identity", "microservice-inventory", "microservice-marketing",
        "microservice-notification", "microservice-order", "microservice-payment",
        "microservice-review", "microservice-search", "microservice-shipping",
        "microservice-shopcart", "microservice-store", "microservice-user"
    ]
    
    gateway_service = ["microservice-gateway"]
    
    # Container name mapping (service name -> container name)
    container_names = {
        "postgres": "karibea-postgres",
        "kafka-0": "karibea-kafka-0",
        "kafka-1": "karibea-kafka-1",
        "kafka-2": "karibea-kafka-2",
        "kafka-init": "karibea-kafka-init",
        "microservice-config": "karibea-config",
        "microservice-eureka": "karibea-eureka",
        "microservice-gateway": "karibea-gateway",
        "microservice-catalog": "karibea-catalog",
        "microservice-chatbot": "karibea-chatbot",
        "microservice-identity": "karibea-identity",
        "microservice-inventory": "karibea-inventory",
        "microservice-marketing": "karibea-marketing",
        "microservice-notification": "karibea-notification",
        "microservice-order": "karibea-order",
        "microservice-payment": "karibea-payment",
        "microservice-review": "karibea-review",
        "microservice-search": "karibea-search",
        "microservice-shipping": "karibea-shipping",
        "microservice-shopcart": "karibea-shopcart",
        "microservice-store": "karibea-store",
        "microservice-user": "karibea-user",
    }
    
    def get_container_name(service):
        return container_names.get(service, service)
    
    try:
        # =====================================================================
        # Phase 0: Infrastructure - PostgreSQL Database
        # =====================================================================
        print("\n" + "="*60)
        print("📊 Phase 0: PostgreSQL Database")
        print("="*60)
        
        if is_container_running(get_container_name("postgres")) and is_container_healthy(get_container_name("postgres")):
            print("   ✅ PostgreSQL is already running and healthy. Skipping...")
        else:
            subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "-d"] + database_services, check=True)
            print("   ⏳ Waiting for PostgreSQL to initialize...")
            if not wait_for_healthy(get_container_name("postgres"), timeout=60):
                print("   ⚠️  PostgreSQL healthcheck timeout, continuing...")
        
        # =====================================================================
        # Phase 0.5: Infrastructure - Kafka Cluster
        # =====================================================================
        print("\n" + "="*60)
        print("📨 Phase 0.5: Kafka Cluster (3 brokers)")
        print("="*60)
        
        kafka_running = all(is_container_running(get_container_name(f"kafka-{i}")) for i in range(3))
        kafka_healthy = all(is_container_healthy(get_container_name(f"kafka-{i}")) for i in range(3))
        
        if kafka_running and kafka_healthy:
            print("   ✅ Kafka cluster is already running and healthy. Skipping...")
        else:
            subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "-d"] + kafka_services, check=True)
            print("   ⏳ Waiting for Kafka cluster to form quorum (this takes ~60s)...")
            time.sleep(60)
        
        # =====================================================================
        # Phase 0.6: Kafka Topics Initialization
        # =====================================================================
        print("\n" + "="*60)
        print("📋 Phase 0.6: Kafka Topics")
        print("="*60)
        subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "-d"] + kafka_init_service, check=True)
        print("   ✅ Kafka topics creation triggered")
        time.sleep(5)
        
        # =====================================================================
        # Phase 1: Config Service (CRITICAL - No rebuild if running)
        # =====================================================================
        print("\n" + "="*60)
        print("⚙️  Phase 1: Config Service (CRITICAL)")
        print("="*60)
        
        if is_container_running(get_container_name("microservice-config")) and is_container_healthy(get_container_name("microservice-config")):
            print("   ✅ Config Service is already running and healthy.")
            print("   ℹ️  Skipping to avoid disrupting dependent services...")
        else:
            # Only build if not running - use 'up -d' without --build if container exists
            result = subprocess.run(
                ["docker", "inspect", get_container_name("microservice-config")],
                capture_output=True, check=False
            )
            if result.returncode == 0:
                # Container exists, just start it
                subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "-d"] + config_service, check=True)
            else:
                # Container doesn't exist, build it
                subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "--build", "-d"] + config_service, check=True)
            
            print("   ⏳ Waiting for Config Service to be healthy...")
            if not wait_for_healthy(get_container_name("microservice-config"), timeout=90):
                print("   ⚠️  Config Service healthcheck timeout, continuing...")
        
        # =====================================================================
        # Phase 2: Eureka Service Discovery (CRITICAL - No rebuild if running)
        # =====================================================================
        print("\n" + "="*60)
        print("🔍 Phase 2: Eureka Service Discovery (CRITICAL)")
        print("="*60)
        
        if is_container_running(get_container_name("microservice-eureka")) and is_container_healthy(get_container_name("microservice-eureka")):
            print("   ✅ Eureka Service is already running and healthy.")
            print("   ℹ️  Skipping to avoid disrupting dependent services...")
        else:
            result = subprocess.run(
                ["docker", "inspect", get_container_name("microservice-eureka")],
                capture_output=True, check=False
            )
            if result.returncode == 0:
                subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "-d"] + eureka_service, check=True)
            else:
                subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "--build", "-d"] + eureka_service, check=True)
            
            print("   ⏳ Waiting for Eureka Service to be healthy...")
            if not wait_for_healthy(get_container_name("microservice-eureka"), timeout=90):
                print("   ⚠️  Eureka Service healthcheck timeout, continuing...")
        
        # =====================================================================
        # Phase 3: Business Microservices
        # =====================================================================
        print("\n" + "="*60)
        print("🚀 Phase 3: Business Microservices (14 services)")
        print("="*60)
        
        # Check which services need to be started
        services_to_start = []
        for svc in other_services:
            container_name = get_container_name(svc)
            if not is_container_running(container_name):
                services_to_start.append(svc)
        
        if not services_to_start:
            print("   ✅ All business microservices are already running. Skipping...")
        else:
            print(f"   📦 Starting {len(services_to_start)} services...")
            # Start all at once without rebuild (images already built)
            subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "-d"] + other_services, check=True)
            print("   ⏳ Waiting 30s for services to register with Eureka...")
            time.sleep(30)
        
        # =====================================================================
        # Phase 4: API Gateway (Last)
        # =====================================================================
        print("\n" + "="*60)
        print("🌐 Phase 4: API Gateway")
        print("="*60)
        
        if is_container_running(get_container_name("microservice-gateway")):
            print("   ✅ Gateway is already running. Refreshing...")
            subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "-d"] + gateway_service, check=True)
        else:
            subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "up", "-d"] + gateway_service, check=True)
        
        print("   ⏳ Waiting for Gateway to start...")
        time.sleep(10)
        
        # =====================================================================
        # Summary
        # =====================================================================
        print("\n" + "="*60)
        print("✅ Successfully started " + environment.upper() + " environment!")
        print("="*60)
        print("\n📝 Quick Access:")
        print("   - PostgreSQL: localhost:5432")
        print("   - Kafka Brokers: localhost:9094, 9095, 9096")
        print("   - Config Server: http://localhost:8888")
        print("   - Eureka Dashboard: http://localhost:8761")
        print("   - API Gateway: http://localhost:8080")
        print("\n💡 Tips:")
        print("   - Use option 7/8 to check service status")
        print("   - Use option 5/6 to view specific logs")
        print("   - Config & Eureka will NOT restart on subsequent runs")
        print("="*60 + "\n")
        
    except subprocess.CalledProcessError as e:
        print(f"\n❌ Error running docker-compose: {e}")
        print("💡 Tip: Check if Docker is running and ports are available")
    except FileNotFoundError:
        print("\n❌ Error: 'docker-compose' command not found.")
        print("💡 Please ensure Docker Compose is installed and in your PATH.")

import concurrent.futures

def build_images(environment):
    """Build all Docker images for the given environment (Parallelized)."""
    if environment not in ['dev', 'prod']:
        print("Invalid environment.")
        return
        
    compose_file = get_compose_file_path(environment)
    if not os.path.exists(compose_file):
        print(f"Error: {compose_file} not found.")
        return
    
    print(f"\n🔨 Building all Docker images for {environment.upper()}...")
    print("⚠️  Building in PARALLEL (max 3) to speed up process.")
    print("This will NOT restart running containers.\n")
    
    # Define service groups
    core_services = ["microservice-config", "microservice-eureka", "microservice-gateway"]
    business_services = [
        "microservice-catalog", "microservice-chatbot", "microservice-identity",
        "microservice-inventory", "microservice-marketing", "microservice-notification",
        "microservice-order", "microservice-payment", "microservice-review",
        "microservice-search", "microservice-shipping", "microservice-shopcart",
        "microservice-store", "microservice-user"
    ]
    
    all_services = core_services + business_services
    total = len(all_services)
    failed_services = []
    
    def build_service(service):
        print(f"   📦 Building {service}...")
        try:
            # Removed --no-cache to allow faster restarts/rebuilds
            result = subprocess.run(
                COMPOSE_CMD + ["-f", compose_file, "build", service],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.PIPE,
                check=False,
                text=True
            )
            if result.returncode != 0:
                print(f"   ❌ Failed to build {service}:\n{result.stderr}")
                return service
            else:
                print(f"   ✅ {service} built successfully")
                return None
        except Exception as e:
            print(f"   ❌ Exception building {service}: {e}")
            return service

    with concurrent.futures.ThreadPoolExecutor(max_workers=3) as executor:
        futures = {executor.submit(build_service, svc): svc for svc in all_services}
        for future in concurrent.futures.as_completed(futures):
            failed = future.result()
            if failed:
                failed_services.append(failed)
        
    print(f"\n{'='*60}")
    if not failed_services:
        print(f"✅ All {total} images built successfully for {environment.upper()}!")
    else:
        print(f"⚠️  Built {total - len(failed_services)}/{total} images.")
        print(f"❌ Failed services: {', '.join(failed_services)}")
    print(f"💡 Use option 1 or 2 to start the environment.")
    print(f"{'='*60}")

def stop_compose(environment):
    if environment not in ['dev', 'prod']:
        print("Invalid environment.")
        return
        
    compose_file = get_compose_file_path(environment)
    if not os.path.exists(compose_file):
        print(f"Error: {compose_file} not found.")
        return
        
    print(f"\n🛑 Stopping {environment.upper()} environment...")
    try:
        subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "down"], check=True)
        print(f"✅ {environment.upper()} environment stopped successfully.")
    except subprocess.CalledProcessError as e:
        print(f"❌ Error stopping services: {e}")

def clean_volumes(environment):
    if environment not in ['dev', 'prod']:
        print("Invalid environment.")
        return
        
    compose_file = get_compose_file_path(environment)
    if not os.path.exists(compose_file):
        print(f"Error: {compose_file} not found.")
        return
    
    print(f"\n⚠️  WARNING: This will remove all volumes for {environment.upper()} environment!")
    print("This includes:")
    print("  - PostgreSQL data")
    print("  - Kafka data (all 3 brokers)")
    print("  - All topic data will be lost")
    
    confirm = input("\nAre you sure? Type 'yes' to confirm: ").strip().lower()
    
    if confirm == 'yes':
        print(f"\n🗑️  Stopping and removing volumes for {environment.upper()}...")
        try:
            subprocess.run(COMPOSE_CMD + [ "-f", compose_file, "down", "-v"], check=True)
            print(f"✅ Volumes cleaned successfully for {environment.upper()}.")
        except subprocess.CalledProcessError as e:
            print(f"❌ Error cleaning volumes: {e}")
    else:
        print("❌ Operation cancelled.")

def view_logs(environment):
    if environment not in ['dev', 'prod']:
        print("Invalid environment.")
        return
        
    compose_file = get_compose_file_path(environment)
    if not os.path.exists(compose_file):
        print(f"Error: {compose_file} not found.")
        return
        
    print(f"\n📜 Showing all logs for {environment.upper()} environment...")
    print("Press Ctrl+C to exit\n")
    cmd = COMPOSE_CMD + [ "-f", compose_file, "logs", "-f"]
    try:
        subprocess.run(cmd)
    except KeyboardInterrupt:
        print("\n⏸️  Stopped viewing logs.")
    except Exception as e:
        print(f"❌ Error viewing logs: {e}")

def view_specific_logs(environment):
    if environment not in ['dev', 'prod']:
        print("Invalid environment.")
        return
        
    compose_file = get_compose_file_path(environment)
    if not os.path.exists(compose_file):
        print(f"Error: {compose_file} not found.")
        return
    
    print("\n" + "="*60)
    print("📜 Select Service to View Logs:")
    print("="*60)
    print("1. 📊 PostgreSQL Database")
    print("2. 📨 Kafka Cluster (all 3 brokers)")
    print("3. 📨 Kafka-0 only")
    print("4. 📨 Kafka-1 only")
    print("5. 📨 Kafka-2 only")
    print("6. ⚙️  Config Service")
    print("7. 🔍 Eureka Service")
    print("8. 🌐 Gateway Service")
    print("9. 🚀 All Microservices")
    print("10. 🎯 Custom service name")
    print("="*60)
    
    choice = input("Enter choice (1-10): ").strip()
    
    services = []
    if choice == '1':
        services = ["postgres"]
    elif choice == '2':
        services = ["kafka-0", "kafka-1", "kafka-2"]
    elif choice == '3':
        services = ["kafka-0"]
    elif choice == '4':
        services = ["kafka-1"]
    elif choice == '5':
        services = ["kafka-2"]
    elif choice == '6':
        services = ["microservice-config"]
    elif choice == '7':
        services = ["microservice-eureka"]
    elif choice == '8':
        services = ["microservice-gateway"]
    elif choice == '9':
        services = [
            "microservice-catalog", "microservice-chatbot",
            "microservice-identity", "microservice-inventory", "microservice-marketing",
            "microservice-notification", "microservice-order", "microservice-payment",
            "microservice-review", "microservice-search", "microservice-shipping",
            "microservice-store", "microservice-user"
        ]
    elif choice == '10':
        service_name = input("Enter service name: ").strip()
        services = [service_name]
    else:
        print("❌ Invalid choice.")
        return
    
    print(f"\n📜 Showing logs for: {', '.join(services)}")
    print("Press Ctrl+C to exit\n")
    cmd = COMPOSE_CMD + ["-f", compose_file, "logs", "-f"] + services
    try:
        subprocess.run(cmd)
    except KeyboardInterrupt:
        print("\n⏸️  Stopped viewing logs.")
    except Exception as e:
        print(f"❌ Error viewing logs: {e}")

def show_status(environment):
    if environment not in ['dev', 'prod']:
        print("Invalid environment.")
        return
        
    compose_file = get_compose_file_path(environment)
    if not os.path.exists(compose_file):
        print(f"Error: {compose_file} not found.")
        return
    
    print(f"\n📊 Status of {environment.upper()} environment:\n")
    try:
        subprocess.run(COMPOSE_CMD + ["-f", compose_file, "ps"], check=True)
    except subprocess.CalledProcessError as e:
        print(f"❌ Error getting status: {e}")

if __name__ == "__main__":
    initialize()
    
    # Check for command line arguments
    if len(sys.argv) > 1:
        command = sys.argv[1].lower()
        if command == 'dev':
            run_compose('dev')
            sys.exit(0)
        elif command == 'prod':
            run_compose('prod')
            sys.exit(0)
        elif command == 'build-dev':
            build_images('dev')
            sys.exit(0)
        elif command == 'build-prod':
            build_images('prod')
            sys.exit(0)
        elif command == 'stop-dev':
            stop_compose('dev')
            sys.exit(0)
        elif command == 'stop-prod':
            stop_compose('prod')
            sys.exit(0)
        elif command == 'clean-dev':
            clean_volumes('dev')
            sys.exit(0)
        elif command == 'clean-prod':
            clean_volumes('prod')
            sys.exit(0)
        else:
            print(f"Unknown command: {command}")
            print("Available commands: dev, prod, build-dev, build-prod, stop-dev, stop-prod, clean-dev, clean-prod")
            sys.exit(1)

    while True:
        print("\n" + "="*60)
        print("🚀 MICROSERVICES DOCKER COMPOSE MANAGER")
        print("="*60)
        print("🏃 START ENVIRONMENT:")
        print("  1. 🟢 Run Development (dev)")
        print("  2. 🔴 Run Production (prod)")
        print("\n🔨 BUILD (without restart):")
        print("  3. Build Images (dev)")
        print("  4. Build Images (prod)")
        print("\n📜 VIEW LOGS:")
        print("  5. View All Logs (dev)")
        print("  6. View All Logs (prod)")
        print("  7. View Specific Logs (dev)")
        print("  8. View Specific Logs (prod)")
        print("\n📊 STATUS:")
        print("  9. Show Status (dev)")
        print("  10. Show Status (prod)")
        print("\n🛑 STOP ENVIRONMENT:")
        print("  11. Stop Development (dev)")
        print("  12. Stop Production (prod)")
        print("\n🗑️  CLEAN:")
        print("  13. Clean Volumes (dev)")
        print("  14. Clean Volumes (prod)")
        print("\n❌ EXIT:")
        print("  15. Exit")
        print("="*60)
        
        choice = input("\n👉 Enter choice (1-15): ").strip()
        
        if choice == '1':
            run_compose('dev')
        elif choice == '2':
            run_compose('prod')
        elif choice == '3':
            build_images('dev')
        elif choice == '4':
            build_images('prod')
        elif choice == '5':
            view_logs('dev')
        elif choice == '6':
            view_logs('prod')
        elif choice == '7':
            view_specific_logs('dev')
        elif choice == '8':
            view_specific_logs('prod')
        elif choice == '9':
            show_status('dev')
        elif choice == '10':
            show_status('prod')
        elif choice == '11':
            stop_compose('dev')
        elif choice == '12':
            stop_compose('prod')
        elif choice == '13':
            clean_volumes('dev')
        elif choice == '14':
            clean_volumes('prod')
        elif choice == '15':
            print("\n👋 Thanks for using Docker Compose Manager. Goodbye!")
            break
        else:
            print("\n❌ Invalid choice. Please enter a number between 1-15.")