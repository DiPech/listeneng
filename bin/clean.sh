#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

docker_postgres_exec() {
    local statement=$1
    ${DOCKER_COMPOSE_EXEC} "${DOCKER_DATABASE}" \
        psql -U "${DOCKER_DATABASE_USER}" -c "${statement}"
}

main() {
    my_echo "Clean application data"
    reject_in_prod "$@"
    disable_strict_mode
    # shellcheck source=stop.sh
    . "${BIN_DIR}/stop.sh"
    docker_remove_volumes "${DOCKER_DATABASE_VOLUME}"
    # shellcheck source=start.sh
    . "${BIN_DIR}/start.sh"
    enable_strict_mode
}

main "$@"
