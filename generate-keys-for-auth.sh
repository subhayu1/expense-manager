#!/usr/bin/env sh

# Create the directory for the keys
mkdir -p ./src/main/resources/certs

# Navigate to the directory
cd ./src/main/resources/certs

# Generate the RSA key pair
openssl genrsa -out keypair.pem 2048

# Generate the public key
openssl rsa -in keypair.pem -pubout -out public.pem

# Create the private key in PKCS#8 format
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem