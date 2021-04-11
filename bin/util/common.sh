#!/usr/bin/env bash

current_script_name=$(readlink "$0" || true)
if [[ -z "${current_script_name}" ]]; then current_script_name="${0}"; fi
scripts_dir=$(dirname "${current_script_name}")

# shellcheck source=util/functions.sh
. "${scripts_dir}/util/functions.sh"

enable_strict_mode

# --- [ Define most popular paths ] ------------------------------------------------------------------------------------

BIN_DIR=$(realpath "${scripts_dir}")
ROOT_DIR=$(dirname "${BIN_DIR}")
DOCKER_DIR="${ROOT_DIR}/docker"
CONF_DIR="${ROOT_DIR}/conf"
DEPLOY_DIR="${ROOT_DIR}/deploy"

# --- [ Load env ] -----------------------------------------------------------------------------------------------------

load_env_from_file "${DOCKER_DIR}/common.env"
load_env_from_file "${CONF_DIR}/app.env"

# --- [ Do some validation ] -------------------------------------------------------------------------------------------

ensure_set "${APP_ENV}"

# --- [ Define most popular commands ] ---------------------------------------------------------------------------------

DOCKER_COMPOSE="docker-compose -f ${DOCKER_DIR}/docker-compose.yml -f ${DOCKER_DIR}/docker-compose.services.yml"
if is_prod; then
    DOCKER_COMPOSE="${DOCKER_COMPOSE} -f ${DOCKER_DIR}/docker-compose.app.yml"
fi
DOCKER_COMPOSE_INTERACTIVE_EXEC="${DOCKER_COMPOSE} exec"
DOCKER_COMPOSE_EXEC="${DOCKER_COMPOSE_INTERACTIVE_EXEC} -T"
