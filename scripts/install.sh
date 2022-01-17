# Install latest versions of modules
COLOR='\033[0;33m'
NC='\033[0m' # No Color
printf "${COLOR} --------- Installing dependencies. --------- ${NC}\n"

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

printf "${COLOR} --------- Done with installation ---------${NC}\n"