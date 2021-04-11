#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

main() {
    local container="$1"
    local shell="${2:-bash}"
    my_echo "Shell into container '${container}'"
    ${DOCKER_COMPOSE_INTERACTIVE_EXEC} "${container}" "${shell}"
}

main "$@"
