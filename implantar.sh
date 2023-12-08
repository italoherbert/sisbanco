
cd $1

./mvnw clean package -DskipTests
docker-compose build $1
docker-compose up $1 &

cd ..
