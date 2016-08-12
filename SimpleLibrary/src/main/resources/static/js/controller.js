library.controller('mainController', function($scope, $http, $uibModal) {

    $scope.loadUsers = function() {
        $http.get('/users/all').then(function(response) {
            var data = response.data;
            if (data) {
                $scope.users = data;
                $scope.users.forEach(function(u, i) {
                    if (u.userDocs && u.userDocs.length > 0) {
                        u.treeData = [{
                            label: "User Docs",
                            id: "role" + i,
                            children: []
                        }];
                        u.userDocs.forEach(function(doc, j) {
                            u.treeData[0].children.push({
                                label: doc.title + " [ISBN " + doc.isbn + "]",
                                id: "role" + i + "" + j,
                                children: []
                            });
                        });
                    }
                });
            }
        });
    };

    $scope.loadDocs = function() {
        $http.get('/docs/all').then(function (response) {
            if (response.data) {
                $scope.docs = response.data;
            }
        });
    };

    $scope.removeUser = function(user) {
        $http.delete('/users/user/' + user.id).then(function(response) {
            $scope.loadUsers();
        });
    };

    $scope.removeDoc = function(doc) {
        $http.delete('/docs/doc/' + doc.id).then(function(response) {
            $scope.loadDocs();
        });
    };

    $scope.createUserModal = function() {
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: '/views/createUserModal.html',
            controller: 'createUserModalCtrl',
            backdrop: 'static',
            keyboard: false
        });
        modalInstance.result.then(function() {
            $scope.loadUsers();
        });
    };

    $scope.createDocumentModal = function() {
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: '/views/createDocumentModal.html',
            controller: 'createDocumentModalCtrl',
            backdrop: 'static',
            keyboard: false
        });
        modalInstance.result.then(function() {
            $scope.loadDocs();
        });
    };

    $scope.loadUsers();
});

library.controller('createUserModalCtrl', function($scope, $http, $uibModalInstance) {
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
        $http.get('/users/user/' + name).then(function (response) {
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
        if (docs.indexOf(selected)==-1) {
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
        $http.post('/users/new', $scope.newUser).then(function(response) {
            $uibModalInstance.close();
        })
    };

    $scope.close = function() {
        $uibModalInstance.dismiss(false);
    };

    function loadDocs() {
        $http.get('/docs/all').then(function(response) {
            if (response.data) {
                $scope.docs = response.data;
            }
        });
    }

    loadDocs();
});

library.controller('createDocumentModalCtrl', function($scope, $http, $uibModalInstance) {
    $scope.ok = function() {
        $http.post('/docs/new', $scope.newDoc).then(function(response) {
            $uibModalInstance.close();
        })
    };

    $scope.close = function() {
        $uibModalInstance.dismiss(false);
    };
});

library.controller('ErrorCtrl', function($scope, $uibModalInstance, errorData) {
    $scope.errorData = errorData;
    $scope.ok = function () {
        $uibModalInstance.close('cancel');
    };
});