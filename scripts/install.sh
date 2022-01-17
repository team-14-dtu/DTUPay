# Install latest versions of modules
BLUE='\033[0;34m'
NC='\033[0m' # No Color
printf "${BLUE} --------- Installing dependencies. --------- ${NC}\n"

pushd libs
pushd messaging-utilities
./install.sh

popd
pushd data
./install.sh

popd
popd
pushd end-to-end-tests
pushd app
./install.sh

popd
popd

printf "${BLUE} --------- Done with installation ---------${NC}\n"