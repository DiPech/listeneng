#!/usr/bin/env bash

# --- [ DOCKER ] -------------------------------------------------------------------------------------------------------

docker_remove_images() {
    local prefix="$1"
    docker image ls | grep "${prefix}" | awk '{print $3}' | xargs docker image remove
}

docker_remove_volumes() {
    local prefix="$1"
    docker volume ls | grep "${prefix}" | awk '{print $2}' | xargs docker volume remove
}

docker_remove_containers() {
    local prefix="$1"
    docker container ls | grep "${prefix}" | awk '{print $1}' | xargs docker container remove
}

docker_remove_networks() {
    local prefix="$1"
    docker network ls | grep "${prefix}" | awk '{print $1}' | xargs docker network remove
}

docker_remove_everything() {
    local prefix="$1"
    docker_remove_images "${prefix}"
    docker_remove_volumes "${prefix}"
    docker_remove_containers "${prefix}"
    docker_remove_networks "${prefix}"
}

docker_info() {
    my_echo "Docker Images"
    docker image ls
    my_echo "Docker Networks"
    docker network ls
    my_echo "Docker Volumes"
    docker volume ls
    my_echo "Docker Containers"
    docker container ls
}

# --- [ ENVIRONMENT ] --------------------------------------------------------------------------------------------------

load_env_from_file() {
    local path=$1
    if [ ! -f "${path}" ]; then
        echo "File «${path}» doesn't exist."
        exit 1
    fi
    set -a
    # shellcheck disable=SC1090
    . "${path}"
    set +a
}

is_set() {
    local value="$1"
    shift
    if [[ -z "${value}" ]]; then
        return 1
    fi
    return 0
}

ensure_set() {
    local value="$1"
    if ! is_set "${value}"; then
        var_name="${!value@}"
        echo "Variable «${var_name}» isn't set."
        exit 1
    fi
}

is_dev() {
    ensure_set "${APP_ENV}"
    if [ "${APP_ENV}" = "dev" ]; then
        return 0
    fi
    return 1
}

is_prod() {
    ensure_set "${APP_ENV}"
    if ! is_dev; then
        return 0
    fi
    return 1
}

reject_in_prod() {
    if is_dev; then
        return 0
    fi
    if has_arg '--force' "$@"; then
        return 0
    fi
    my_echo "PROD detected, action is rejected, pass '--force' to permit!"
    exit 1
}

##
## Checks arg for existence. Example:
## if has_arg '--some-flag' "$@"; then
##   echo "Has!"
## fi
##
has_arg() {
    local term="$1"
    shift
    for arg; do
        if [[ $arg == "$term" ]]; then
            return 0
        fi
    done
    return 1
}

# --- [ FILES AND PATHS ] ----------------------------------------------------------------------------------------------

is_file_exists() {
    local path="$1"
    if ! test -f "${path}"; then
        return 1
    fi
    return 0
}

ensure_file_exists() {
    local path="$1"
    if ! is_file_exists "${path}"; then
        echo "File «${path}» doesn't exist."
        exit 1
    fi
}

##
## example: get_relative_path "/a/b/c" "/a/b/c/d/e" => "d/e"
##
get_relative_path() {
    local common_dir="$1/"
    local absolute_path=$2
    echo "${absolute_path#$common_dir}"
}

# --- [ MISCELLANEOUS ] ------------------------------------------------------------------------------------------------

my_echo() {
    local message="$1"
    shift
    echo ">>>>> ${message}"
}

enable_strict_mode() {
    set -euo pipefail
}

disable_strict_mode() {
    set +euo pipefail
}
