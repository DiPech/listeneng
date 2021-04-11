#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

main() {
    my_echo "Update services"
    # shellcheck source=build.sh
    . "${BIN_DIR}/build.sh"
    # shellcheck source=stop.sh
    . "${BIN_DIR}/stop.sh"
    if has_arg '--up' "$@"; then
        # shellcheck source=up.sh
        . "${BIN_DIR}/up.sh"
        return 0
    fi
    # shellcheck source=start.sh
    . "${BIN_DIR}/start.sh"
}

main "$@"
