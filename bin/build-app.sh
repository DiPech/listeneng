#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

switch_to_right_java_sdk() {
    disable_strict_mode
    # shellcheck disable=SC1090
    # shellcheck disable=SC2088
    . "${HOME}/.sdkman/bin/sdkman-init.sh"
    sdk use java "${JAVA_SDK_VERSION}"
    enable_strict_mode
}

main() {
    my_echo "Build application"
    my_echo "Switch to the JDK ${JAVA_SDK_VERSION}"
    switch_to_right_java_sdk
    "${ROOT_DIR}/mvnw" clean package
}

main
