import socket
import threading

def receive_messages(sock):
    """Background thread to handle incoming messages from the server."""
    while True:
        try:
            data = sock.recv(1024)
            if not data:
                print("\n[INFO] Serverul a închis conexiunea.")
                break
            # Use \r to clear the current line and print the message
            print(f"\n[SERVER]: {data.decode('utf-8').strip()}")
            print("Tu: ", end="", flush=True)
        except:
            break

def start_bidirectional_chat():
    SERVER_IP = "100.112.203.47" 
    SERVER_PORT = 65432

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        client_socket.connect((SERVER_IP, SERVER_PORT))
        print("✅ Conectat la Server ! " + SERVER_IP +  " Poți tasta mesaje oricând.")

        # START THE LISTENING THREAD
        threading.Thread(target=receive_messages, args=(client_socket,), daemon=True).start()

        # MAIN LOOP FOR TYPING
        while True:
            my_msg = input("Tu: ") # Now the prompt is always available!
            if my_msg.lower() == 'exit':
                break
            client_socket.sendall((my_msg + "\n").encode('utf-8'))

    except Exception as e:
        print(f"❌ Eroare: {e}")
    finally:
        client_socket.close()
        print("Conexiune închisă.")

if __name__ == "__main__":
    start_bidirectional_chat()