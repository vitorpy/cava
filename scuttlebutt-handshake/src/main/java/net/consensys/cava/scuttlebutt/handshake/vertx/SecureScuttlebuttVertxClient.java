/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.consensys.cava.scuttlebutt.handshake.vertx;


import net.consensys.cava.bytes.Bytes;
import net.consensys.cava.bytes.Bytes32;
import net.consensys.cava.concurrent.AsyncCompletion;
import net.consensys.cava.concurrent.CompletableAsyncCompletion;
import net.consensys.cava.crypto.sodium.Signature;
import net.consensys.cava.scuttlebutt.handshake.HandshakeException;
import net.consensys.cava.scuttlebutt.handshake.SecureScuttlebuttHandshakeClient;
import net.consensys.cava.scuttlebutt.handshake.SecureScuttlebuttStreamClient;
import net.consensys.cava.scuttlebutt.handshake.SecureScuttlebuttStreamServer;
import net.consensys.cava.scuttlebutt.handshake.StreamException;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

/**
 * Secure Scuttlebutt client using Vert.x to manage persistent TCP connections.
 *
 */
public final class SecureScuttlebuttVertxClient {

  private class NetSocketClientHandler {

    private final NetSocket socket;
    private final SecureScuttlebuttHandshakeClient handshakeClient;
    private final ClientHandlerFactory handlerFactory;
    private int handshakeCounter;
    private SecureScuttlebuttStreamClient client;
    private ClientHandler handler;

    NetSocketClientHandler(NetSocket socket, Signature.PublicKey remotePublicKey, ClientHandlerFactory handlerFactory) {
      this.socket = socket;
      this.handshakeClient = SecureScuttlebuttHandshakeClient.create(keyPair, networkIdentifier, remotePublicKey);
      this.handlerFactory = handlerFactory;
      socket.closeHandler(res -> {
        if (handler != null) {
          handler.streamClosed();
        }
      });
      socket.handler(this::handle);
      socket.write(Buffer.buffer(handshakeClient.createHello().toArrayUnsafe()));
    }

    void handle(Buffer buffer) {
      try {
        if (handshakeCounter == 0) {
          handshakeClient.readHello(Bytes.wrapBuffer(buffer));
          socket.write(Buffer.buffer(handshakeClient.createIdentityMessage().toArrayUnsafe()));
          handshakeCounter++;
        } else if (handshakeCounter == 1) {
          handshakeClient.readAcceptMessage(Bytes.wrapBuffer(buffer));
          client = handshakeClient.createStream();
          this.handler = handlerFactory
              .createHandler(bytes -> socket.write(Buffer.buffer(client.sendToServer(bytes).toArrayUnsafe())), () -> {
                socket.write(Buffer.buffer(client.sendGoodbyeToServer().toArrayUnsafe()));
                socket.close();
              });
          handshakeCounter++;
        } else {
          Bytes message = client.readFromServer(Bytes.wrapBuffer(buffer));
          if (SecureScuttlebuttStreamServer.isGoodbye(message)) {
            socket.close();
          } else {
            handler.receivedMessage(message);
          }
        }
      } catch (HandshakeException | StreamException e) {
        socket.close();
      }
    }
  }

  private final Vertx vertx;
  private final Signature.KeyPair keyPair;
  private final Bytes32 networkIdentifier;
  private NetClient client;

  /**
   * Default constructor.
   *
   * @param vertx the Vert.x instance
   * @param keyPair the identity of the server according to the Secure Scuttlebutt protocol
   * @param networkIdentifier the network identifier of the server according to the Secure Scuttlebutt protocol
   */
  public SecureScuttlebuttVertxClient(Vertx vertx, Signature.KeyPair keyPair, Bytes32 networkIdentifier) {
    this.vertx = vertx;
    this.keyPair = keyPair;
    this.networkIdentifier = networkIdentifier;
  }

  /**
   * Connects the client to a remote host.
   *
   * @param port the port of the remote host
   * @param host the host string of the remote host
   * @param remotePublicKey the public key of the remote host
   * @param handlerFactory the factory of handlers for connections
   * @return a handle to a new stream handler with the remote host
   */
  public AsyncCompletion connectTo(
      int port,
      String host,
      Signature.PublicKey remotePublicKey,
      ClientHandlerFactory handlerFactory) {
    client = vertx.createNetClient(new NetClientOptions().setTcpKeepAlive(true));
    CompletableAsyncCompletion completion = AsyncCompletion.incomplete();
    client.connect(port, host, res -> {
      if (res.failed()) {
        completion.completeExceptionally(res.cause());
      } else {
        NetSocket socket = res.result();
        new NetSocketClientHandler(socket, remotePublicKey, handlerFactory);
        completion.complete();
      }
    });

    return completion;
  }

  /**
   * Stops the server.
   *
   * @return a handle to the completion of the operation
   */
  public AsyncCompletion stop() {
    client.close();
    return AsyncCompletion.completed();
  }
}
