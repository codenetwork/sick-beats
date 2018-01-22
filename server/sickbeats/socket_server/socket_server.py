import socket
import sys

import os
import _thread

HOST = os.getenv('SERVER_HOST', '')
PORT = os.getenv('SERVER_POST', 57438)


def client_listener(conn):
    while True:
        data = conn.recv(1024)
        if not data:
            break
        print(data)

    conn.close()


def _socket_server():
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
        _thread.start_new_thread(client_listener, (conn,))


def setup_socket_server():
    _thread.start_new_thread(_socket_server, ())
