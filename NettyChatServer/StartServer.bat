set /p port="Server port: "
mvn exec:java -D"exec.mainClass"="server.ChatServer" -Dexec.args=%port%