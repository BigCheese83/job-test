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