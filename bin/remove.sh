#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

main() {
    my_echo "Remove old docker objects"
    reject_in_prod "$@"
    # shellcheck source=stop.sh
    . "${BIN_DIR}/stop.sh"
    disable_strict_mode
    docker_remove_everything "${COMMON_PREFIX}"
    enable_strict_mode
}

main "$@"
