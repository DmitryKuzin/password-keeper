#mvn clean package
docker build . -t dmitriykuzin/password-keeper:1.0.4
docker push dmitriykuzin/password-keeper:1.0.4
