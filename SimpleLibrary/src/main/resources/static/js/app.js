var library = angular.module('library', [
    'ui.bootstrap',
    'ui.bootstrap.tpls',
    'angularTreeview',
    'angular-confirm'
]);

library.factory('errorInterceptor', ['$q', '$injector',
    function ($q, $injector) {
        return {
            'response': function (response) {
                return response;
            },
            'responseError': function (rejection) {
                switch (rejection.status) {
                    case 500:
                    {
                        var globalErrorHandler = $injector.get('globalErrorHandler');
                        var errorData = rejection.data;
                        if (errorData) {
                            globalErrorHandler.showError(errorData);
                        }
                        break;
                    }
                }
                return $q.reject(rejection);
            }
        };
    }
]);

library.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('errorInterceptor');
}]);

library.service('globalErrorHandler', ['$uibModal', '$uibModalStack',
    function ($uibModal, $uibModalStack) {
        this.showError = function (errorData) {
            $uibModalStack.dismissAll();
            var modalInstance = $uibModal.open({
                templateUrl: '/views/globalError.html',
                controller: 'ErrorCtrl',
                backdropClass: 'modal-backdrop fade in',
                backdrop: 'static',
                windowClass: 'red',
                keyboard: false,
                resolve: {
                    errorData: function() {
                        return errorData;
                    }
                }
            });
        }
    }]);

library.service('LibraryService', function($http, $uibModal) {
    return {
        loadUsers: function(pageRequest) {
            return $http.get('/users/all?page='+ (pageRequest.page-1) +'&size='+ pageRequest.size);
        },

        loadDocs: function(pageRequest) {
            return $http.get('/docs/all?page='+ (pageRequest.page-1) +'&size='+ pageRequest.size);
        },

        findUser: function (name) {
            return $http.get('/users/user/' + name);
        },

        removeUser: function (user) {
            return $http.delete('/users/user/' + user.id);
        },

        removeDoc: function (doc) {
            return $http.delete('/docs/doc/' + doc.id);
        },

        createUser: function (user) {
            return $http.post('/users/new', user);
        },

        createDocument: function(doc) {
            return $http.post('/docs/new', doc);
        },

        searchDocument: function (str) {
            return $http.get('/docs/search?str=' + str);
        },

        showCreateUserModal: function () {
            return $uibModal.open({
                animation: true,
                templateUrl: '/views/createUserModal.html',
                controller: 'createUserModalCtrl',
                backdrop: 'static',
                keyboard: false
            });
        },

        showCreateDocumentModal: function () {
            return $uibModal.open({
                animation: true,
                templateUrl: '/views/createDocumentModal.html',
                controller: 'createDocumentModalCtrl',
                backdrop: 'static',
                keyboard: false
            });
        },

        getPagination: function(data) {
            return {
                totalElements: data.totalElements,
                totalPages: data.totalPages,
                size: data.size
            };
        }

    };
});