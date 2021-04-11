#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

main() {
    my_echo "Show docker logs"
    if has_arg '--attach' "$@"; then
        ${DOCKER_COMPOSE} logs --follow
        return 0
    fi
    ${DOCKER_COMPOSE} logs
}

main "$@"
