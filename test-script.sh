#!/bin/bash

VERSION=0.1
WIREMOCK_BASE_URL=http://localhost:8080

curl_tracker() { curl -s $WIREMOCK_BASE_URL/__admin/mappings_tracker/"$@"; }
curl_mappings() { curl -s $WIREMOCK_BASE_URL/__admin/mappings "$@"; }
curl_tracker_uuids() { curl_tracker "$@" | jq -c '[.[].id]'; }
get_mapping_uuids() { curl_mappings | jq -c '[.mappings[].id]'; }
add_mapping() { curl_mappings -d '{ "request": { "url": "'$1'" } }' | jq -c '.uuid'; }

assert() {
    if [ $2 ]; then
        echo "[ PASS ] $1"
    else
        echo -e "[\e[31mFAIL\e[0m] $1: \"$2\""
    fi
}

start_test() {
    echo -e "\n$1:"
    reset
}

reset() {
    curl_tracker reset -X POST > /dev/null
    curl_mappings -X DELETE > /dev/null
}

echo "Launching Wiremock standalone"
java -jar build/libs/wiremock-stub-mapping-tracker-$VERSION-standalone.jar &
WIREMOCK_PID=$!
trap "kill $WIREMOCK_PID" exit

echo -n "Waiting for Wiremock to start up."
until $(curl --output /dev/null --silent --head ${WIREMOCK_BASE_URL}/__admin); do
    echo -n '.'
    sleep 1
done


start_test "Creating 1 mapping and call DELETE /__admin/mappings_tracker/unmatched"
ID1=$(add_mapping '/foo')
assert "check mapping is unmatched" "$(curl_tracker_uuids unmatched) == [$ID1]"
assert "check nothing is matched" "$(curl_tracker_uuids matched) == []"
curl_tracker unmatched -X DELETE
assert "check mapping was deleted" "$(get_mapping_uuids) == []"
assert "check nothing is unmatched" "$(curl_tracker_uuids unmatched) == []"

start_test "Creating 2 mappings, make one matching request, then call DELETE"
ID1=$(add_mapping '/foo')
ID2=$(add_mapping '/bar')
curl -s $WIREMOCK_BASE_URL/foo > /dev/null
assert "check first mapping is unmatched" "$(curl_tracker_uuids unmatched) == [$ID2]"
assert "check second mapping is matched" "$(curl_tracker_uuids matched) == [$ID1]"
curl_tracker unmatched -s -X DELETE
assert "check second mappings was deleted" "$(get_mapping_uuids) == [$ID1]"
assert "check nothing is unmatched" "$(curl_tracker_uuids unmatched) == []"

start_test "Trying with both requests matching (should not delete any)"
ID1=$(add_mapping '/foo')
ID2=$(add_mapping '/bar')
curl -s $WIREMOCK_BASE_URL/foo > /dev/null
curl -s $WIREMOCK_BASE_URL/bar > /dev/null
curl_tracker unmatched -s -X DELETE
assert "check no mapping was deleted" "$(get_mapping_uuids) == [$ID2,$ID1]"
assert "check nothing is unmatched" "$(curl_tracker_uuids unmatched) == []"
assert "check both are matched" "$(curl_tracker_uuids matched) == [$ID2,$ID1]"
