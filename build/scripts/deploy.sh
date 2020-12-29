cd ../../
mvn clean install
cd build
# Deploy
docker-compose up --build
