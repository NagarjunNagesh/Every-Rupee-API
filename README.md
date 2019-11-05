# bb-core

Use run it in the EC2
java -jar changeme.jar &

Transfer the jar file to ec2
scp -i pathreference/to/file.pem /path/to/file.jar user@awsenpoint:~


Kill Process in EC2
ps -aux | grep bb-core
kill $PID
