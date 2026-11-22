#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")"
if [[ -z "${JAVA_HOME:-}" && -d '/Applications/Android Studio.app/Contents/jbr/Contents/Home' ]]; then
    export JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home'
fi
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
# Release signing: the BEEB_KEYSTORE_* variables, or else a properties file kept outside the
# repo (storeFile / keyAlias / storePassword) — by default ~/Keystores/beeb-plus-release.properties.
PROPS="${BEEB_KEYSTORE_PROPERTIES:-$HOME/Keystores/beeb-plus-release.properties}"
if [[ -z "${BEEB_KEYSTORE_PATH:-}" && -r "$PROPS" ]]; then
    prop() { sed -n "s/^$1=//p" "$PROPS" | head -1; }
    export BEEB_KEYSTORE_PATH="$(prop storeFile)" BEEB_KEY_ALIAS="$(prop keyAlias)" BEEB_KEYSTORE_PASSWORD="$(prop storePassword)"
fi
if [[ $# -eq 0 ]]; then set -- assembleDebug; fi
exec ./gradlew --no-daemon "$@"
