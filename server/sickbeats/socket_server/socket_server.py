import socket
import sys
import os
from threading import Thread

HOST = os.getenv('SERVER_HOST', '')
PORT = os.getenv('SERVER_POST', 57438)

sock = None


def client_listener(conn):
    while True:
        data = conn.recv(1024)
        if not data:
            break
        print(data)
        conn.send(b'{"type":"close"}')

    conn.close()


def _socket_server():
    global sock
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        sock.bind((HOST, PORT))
    except socket.error as e:
        print('Failed to bind to port: ' + e.strerror)
        sys.exit(-1)

    sock.listen(10)  # 10 for now. Give it a proper number later, maybe setup some sharding.

    print('Started socket listening on port {}'.format(PORT))

    while True:
        conn, addr = sock.accept()
        print('Connected with ' + addr[0] + ':' + str(addr[1]))
        Thread(target=client_listener, args=(conn,)).start()


def setup_socket_server():
    Thread(target=_socket_server).start()


def close_socker_server():
    # TODO
    pass
