#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

BUNDLE_NAME="bundle.tgz"
REMOTE_APP_DIR="/root/listeneng"

create_app_tgz() {
    local excludes="--exclude-from=${DEPLOY_DIR}/exclude.lst"
    # shellcheck disable=SC2164
    cd "${ROOT_DIR}"
    tar -pczf "${DEPLOY_DIR}/${BUNDLE_NAME}" -C "${ROOT_DIR}" "${excludes}" . >/dev/null 2>&1
    # shellcheck disable=SC2164
    cd -
}

copy_to_remote_server() {
    # shellcheck disable=SC2029
    ssh listeneng "mkdir -p ${REMOTE_APP_DIR}"
    scp "${DEPLOY_DIR}/${BUNDLE_NAME}" listeneng:"${REMOTE_APP_DIR}/${BUNDLE_NAME}"
}

run_deployment_remote() {
    local cd_cmd="cd ${REMOTE_APP_DIR}";
    local untar_cmd="tar zxvf bundle.tgz";
    local copy_env_cmd="cp ./conf/app.prod.env ./conf/app.env";
    local update_cmd="bash run update";
    # shellcheck disable=SC2029
    ssh listeneng "${cd_cmd};${untar_cmd};${copy_env_cmd};${update_cmd};"
}

main() {
    reject_in_prod "$@"
    # shellcheck source=build-app.sh
    . "${BIN_DIR}/build-app.sh"
    my_echo "Create TGZ"
    create_app_tgz
    my_echo "Copy to the remote server"
    copy_to_remote_server
    my_echo "Run installation on the remove server"
    run_deployment_remote
}

main "$@"
