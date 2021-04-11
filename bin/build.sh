#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/common.sh
. "${scripts_dir}/util/common.sh"

build_database_docker_image() {
    docker image build \
        -t "${COMMON_PREFIX}-${DOCKER_DATABASE}" \
        -f "./docker/${DOCKER_DATABASE}/Dockerfile" \
        "./docker/${DOCKER_DATABASE}"
}

build_http_proxy_docker_config() {
    # shellcheck disable=SC2016
    envsubst '$${APP_DOMAIN}' \
        <"${DOCKER_DIR}/${DOCKER_HTTP_PROXY}/conf/nginx.conf" \
        >"${DOCKER_DIR}/${DOCKER_HTTP_PROXY}/build/nginx.conf"
}

build_app_docker_image() {
    dist_dir="${DOCKER_DIR}/${DOCKER_APP}/dist"
    ensure_file_exists "${dist_dir}/app.war"
    ensure_file_exists "${dist_dir}/app.env"
    docker image build \
        -t "${COMMON_PREFIX}-${DOCKER_APP}" \
        -f "./docker/${DOCKER_APP}/Dockerfile" \
        "./docker/${DOCKER_APP}"
}

main() {
    my_echo "Build database image"
    build_database_docker_image
    if is_dev; then
        return 0
    fi
    my_echo "Build http-proxy config"
    build_http_proxy_docker_config
    my_echo "Build application image"
    build_app_docker_image
}

main
