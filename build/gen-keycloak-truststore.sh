
echo | openssl s_client -connect localhost:8443 2>&1 | \
    sed --quiet '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' \
    > keycloak-trust.pem

keytool -import -trustcacerts -noprompt -storepass 654321 -alias keycloak -file keycloak-trust.pem -keystore keycloak-trust.keystore