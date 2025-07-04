generate-proto:
	@mkdir -p build/generated/proto
	@smithytranslate smithy-to-proto --input src/main/smithy build/generated/proto

inject-protovalidate-validations:
	@python3 scripts/smithy-protovalidate/src/main/python/inject_protovalidate.py ./src/main/smithy ./build/generated/proto

buf-codegen:
	@buf generate

pre-build: generate-proto inject-protovalidate-validations buf-codegen

build-gradle:
	@./gradlew build

build: pre-build build-gradle

format-smithy:
	@smithy format src/main/smithy

format: format-smithy

test: pre-build
	@./gradlew test

test-coverage-verification: test
	@./gradlew jacocoTestCoverageVerification

clean:
	@./gradlew clean

version:
	@./gradlew --version

upgrade:
	@if [ -z "$(GRADLE_VERSION)" ]; then \
		echo "Warning: GRADLE_VERSION is not set. Usage: make upgrade GRADLE_VERSION=X.Y.Z"; \
	else \
		./gradlew wrapper --gradle-version $(GRADLE_VERSION) --distribution-type all; \
	fi