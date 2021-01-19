cd ../
# Run
export javaOpts='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'
docker-compose up --build