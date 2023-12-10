#GERA O KEYSTORE COM PAR DE CHAVES
keytool -genkeypair -alias keycloak -storepass 123456 -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore keycloak.keystore

# EXPORTA O CERTIFICADO
#keytool -selfcert -alias keycloak -keystore keycloak.keystore -rfc -storepass 123456

# CRIA A REQUISIÇÃO PARA ASSINATURA DO CERTIFICADO
#keytool -certreq -alias keycloak -keystore keycloak.keystore -storepass 123456 > keycloak.cer

# IMPORTA O CERTIFICADO ASSINADO, ADICIONANDO-O EM UM NOVO KEYSTORE
#keytool -exportcert -alias keycloak -file keycloak.cert -keystore keycloak.keystore -storepass 123456

#keytool -import -trustcacerts -noprompt -storepass 654321 -alias keycloak -file keycloak.cert -keystore keycloak.jks