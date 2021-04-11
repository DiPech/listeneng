#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

main() {
    my_echo "Stop services"
    disable_strict_mode
    ${DOCKER_COMPOSE} down --rmi=local --remove-orphans
    enable_strict_mode
}

main
