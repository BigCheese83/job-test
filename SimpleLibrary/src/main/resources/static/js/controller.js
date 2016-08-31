library.controller('mainController', function($scope, LibraryService) {
    $scope.users = {};
    $scope.docs = {};
    $scope.userPageRequest = {page: 1, size: 10};
    $scope.docPageRequest = {page: 1, size: 10};

    $scope.getUsers = function(pageRequest) {
        if (!pageRequest) {
            $scope.userPageRequest.page = 1;
        } else {
            $scope.userPageRequest = pageRequest;
        }
        LibraryService.loadUsers($scope.userPageRequest).then(function(response) {
            $scope.users.data = response.data.content;
            $scope.users.pagination = LibraryService.getPagination(response.data);

            $scope.users.data.forEach(function(u, i) {
                if (u.userDocs && u.userDocs.length > 0) {
                    u.treeData = [{
                        id: "role" + i, label: "User Docs", children: []
                    }];
                    u.userDocs.forEach(function(doc, j) {
                        u.treeData[0].children.push({
                            id: "role" + i + "" + j, label: doc.title + " [ISBN " + doc.isbn + "]", children: []
                        });
                    });
                }
            });
        });
    };

    $scope.getDocs = function(pageRequest) {
        if (!pageRequest) {
            $scope.docPageRequest.page = 1;
        } else {
            $scope.docPageRequest = pageRequest;
        }
        LibraryService.loadDocs($scope.docPageRequest).then(function (response) {
            $scope.docs.data = response.data.content;
            $scope.docs.pagination = LibraryService.getPagination(response.data);
        });
    };

    $scope.removeUser = function(user) {
        LibraryService.removeUser(user).then(function(response) {
            $scope.getUsers();
        });
    };

    $scope.removeDoc = function(doc) {
        LibraryService.removeDoc(doc).then(function(response) {
            $scope.getDocs();
        });
    };

    $scope.createUserModal = function() {
        LibraryService.showCreateUserModal().result.then(function() {
            $scope.getUsers();
        });
    };

    $scope.createDocumentModal = function() {
        LibraryService.showCreateDocumentModal().result.then(function() {
            $scope.getDocs();
        });
    };

    $scope.$watch('userPageRequest.size', function(){
        $scope.getUsers();
    });

    $scope.$watch('docPageRequest.size', function(){
        $scope.getDocs();
    });

    $scope.getUsers();
});

library.controller('createUserModalCtrl', function($scope, $uibModalInstance, LibraryService) {
    var views = ['createUser', 'selectDoc'];

    $scope.selectedView = views[0];
    $scope.newUser = {};
    $scope.typeAhead = {
        title: null,
        item: null,
        onSelect: function($item) {
            this.item = $item;
        }
    };

    $scope.findUser = function(name) {
        LibraryService.findUser(name).then(function (response) {
            if (response.data) {
                $scope.newUser.userDocs = response.data.userDocs;
            } else {
                $scope.newUser.userDocs = null;
            }
        });
    };

    $scope.selectClick = function() {
        $scope.typeAhead.item = null;
        $scope.typeAhead.title = null;
        $scope.selectedView = views[1];
    };

    $scope.selectDocument = function() {
        if (!$scope.newUser.userDocs) {
            $scope.newUser.userDocs = [];
        }
        var docs = $scope.newUser.userDocs;
        var selected = $scope.typeAhead.item;
        if (angular.isObject(selected) && selected.hasOwnProperty("id")
            && docs.every(function(val) {
                return val.id != selected.id;
            })) {
            docs.push(selected);
        }
        $scope.selectedView = views[0];
    };

    $scope.removeDocument = function(doc) {
        var index = $scope.newUser.userDocs.indexOf(doc);
        if (index > -1) {
            $scope.newUser.userDocs.splice(index, 1);
        }
    };

    $scope.back = function() {
        $scope.selectedView = views[0];
    };

    $scope.ok = function() {
        LibraryService.createUser($scope.newUser).then(function(response) {
            $uibModalInstance.close();
        })
    };

    $scope.close = function() {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.lookup = function(str) {
        return LibraryService.searchDocument(str).then(function (response) {
            return response.data;
        });
    };
});

library.controller('createDocumentModalCtrl', function($scope, $uibModalInstance, LibraryService) {
    $scope.ok = function() {
        LibraryService.createDocument($scope.newDoc).then(function(response) {
            $uibModalInstance.close();
        });
    };

    $scope.close = function() {
        $uibModalInstance.dismiss('cancel');
    };
});

library.controller('ErrorCtrl', function($scope, $uibModalInstance, errorData) {
    $scope.errorData = errorData;

    $scope.ok = function () {
        $uibModalInstance.close('ok');
    };
});