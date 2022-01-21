# Install latest versions of modules
# @author : Petr
COLOR='\033[0;33m'
NC='\033[0m' # No Color
printf "${COLOR} --------- Installing dependencies. --------- ${NC}\n"

if [[ "$PWD" == *scripts ]]
then
  cd ..
fi


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