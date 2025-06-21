import { useEffect, useRef, useState } from "react";

interface ChatMessage {
  message: string;
  sender: string;
  date: string;
}

function App() {
  const BASE_URL = "://localhost:8080";
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [currentUser, setCurrentUser] = useState("");
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const [socket, setSocket] = useState<WebSocket | null>(null);
  const [loginError, setLoginError] = useState("");
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleLogin = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoginError("");

    try {
      const response = await fetch(`http${BASE_URL}/api/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        setCurrentUser(username);
        setIsLoggedIn(true);
        connectWebSocket();
      } else {
        setLoginError("Invalid username or password");
      }
    } catch (error) {
      setLoginError("Connection error");
    }
  };

  const connectWebSocket = () => {
    const webSocket = new WebSocket(`ws${BASE_URL}/chat`);

    webSocket.onopen = () => {
      console.log("Connected to WebSocket");
    };

    webSocket.onmessage = (event) => {
      const message: ChatMessage = JSON.parse(event.data);
      setMessages((prev) => [...prev, message]);
    };

    webSocket.onclose = () => {
      console.log("WebSocket connection closed");
    };

    webSocket.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    setSocket(webSocket);
  };

  const sendMessage = (event: React.FormEvent) => {
    event.preventDefault();

    if (newMessage.trim() && socket) {
      const message: ChatMessage = {
        message: newMessage.trim(),
        sender: currentUser,
        date: "",
      };

      socket.send(JSON.stringify(message));
      setNewMessage("");
    }
  };

  const handleLogout = () => {
    if (socket) {
      socket.close();
    }

    setIsLoggedIn(false);
    setCurrentUser("");
    setMessages([]);
    setUsername("");
    setPassword("");
    setLoginError("");
  };

  if (!isLoggedIn) {
    return (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
          backgroundColor: "#f0f0f0",
        }}
      >
        <div
          style={{
            backgroundColor: "white",
            padding: "2rem",
            borderRadius: "8px",
            boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
            width: "300px",
          }}
        >
          <h2
            style={{
              textAlign: "center",
              marginBottom: "1.5rem",
              color: "#333",
            }}
          >
            Chat Login
          </h2>
          <div>
            <div style={{ marginBottom: "1rem" }}>
              <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                style={{
                  width: "100%",
                  padding: "0.75rem",
                  border: "1px solid #ddd",
                  borderRadius: "4px",
                  fontSize: "1rem",
                }}
                onKeyDown={(e) => e.key === "Enter" && handleLogin(e)}
              />
            </div>
            <div style={{ marginBottom: "1rem" }}>
              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={{
                  width: "100%",
                  padding: "0.75rem",
                  border: "1px solid #ddd",
                  borderRadius: "4px",
                  fontSize: "1rem",
                }}
                onKeyDown={(e) => e.key === "Enter" && handleLogin(e)}
              />
            </div>
            {loginError && (
              <div
                style={{
                  color: "#e74c3c",
                  fontSize: "0.875rem",
                  marginBottom: "1rem",
                  textAlign: "center",
                }}
              >
                {loginError}
              </div>
            )}
            <button
              onClick={handleLogin}
              style={{
                width: "100%",
                padding: "0.75rem",
                backgroundColor: "#3498db",
                color: "white",
                border: "none",
                borderRadius: "4px",
                fontSize: "1rem",
                cursor: "pointer",
              }}
            >
              Login
            </button>
          </div>
          <div
            style={{
              marginTop: "1rem",
              fontSize: "0.875rem",
              color: "#666",
              textAlign: "center",
            }}
          >
            Test users: john/password123, jane/password456, admin/admin
          </div>
        </div>
      </div>
    );
  }

  return (
    <div
      style={{
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        backgroundColor: "#f8f9fa",
      }}
    >
      {/* Header */}
      <div
        style={{
          backgroundColor: "#3498db",
          color: "white",
          padding: "1rem",
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <h1 style={{ margin: 0, fontSize: "1.5rem" }}>Public Chat</h1>
        <div style={{ display: "flex", alignItems: "center", gap: "1rem" }}>
          <span>Welcome, {currentUser}!</span>
          <button
            onClick={handleLogout}
            style={{
              padding: "0.5rem 1rem",
              backgroundColor: "#e74c3c",
              color: "white",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
            }}
          >
            Logout
          </button>
        </div>
      </div>

      {/* Messages Area */}
      <div
        style={{
          flex: 1,
          padding: "1rem",
          overflowY: "auto",
          backgroundColor: "white",
          margin: "1rem",
          borderRadius: "8px",
          boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
        }}
      >
        {messages.length === 0 ? (
          <div
            style={{
              textAlign: "center",
              color: "#666",
              fontStyle: "italic",
              marginTop: "2rem",
            }}
          >
            No messages yet. Start the conversation!
          </div>
        ) : (
          messages.map((message, index) => (
            <div
              key={index}
              style={{
                marginBottom: "1rem",
                padding: "0.75rem",
                backgroundColor:
                  message.sender === currentUser ? "#e3f2fd" : "#f5f5f5",
                borderRadius: "8px",
                borderLeft: `4px solid ${
                  message.sender === currentUser ? "#2196f3" : "#9e9e9e"
                }`,
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  marginBottom: "0.25rem",
                  fontSize: "0.875rem",
                  color: "#666",
                }}
              >
                <strong
                  style={{
                    color:
                      message.sender === currentUser ? "#1976d2" : "#424242",
                  }}
                >
                  {message.sender}
                </strong>
                <span>{message.date}</span>
              </div>
              <div style={{ fontSize: "1rem", color: "#333" }}>
                {message.message}
              </div>
            </div>
          ))
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Message Input */}
      <div
        style={{
          padding: "1rem",
          backgroundColor: "white",
          borderTop: "1px solid #ddd",
        }}
      >
        <div style={{ display: "flex", gap: "0.5rem" }}>
          <input
            type="text"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            placeholder="Type your message..."
            onKeyDown={(e) => e.key === "Enter" && sendMessage(e)}
            style={{
              flex: 1,
              padding: "0.75rem",
              border: "1px solid #ddd",
              borderRadius: "4px",
              fontSize: "1rem",
            }}
          />
          <button
            onClick={sendMessage}
            style={{
              padding: "0.75rem 1.5rem",
              backgroundColor: "#27ae60",
              color: "white",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
              fontSize: "1rem",
            }}
          >
            Send
          </button>
        </div>
      </div>
    </div>
  );
}

export default App;
