'use strict';


angular.module('webApp.approveddistributers', ['ngRoute', 'firebase'])

.config(['$routeProvider', function($routeProvider){
	$routeProvider.when('/approveddistributers',{
		templateUrl: 'Distributer/approveddistributer.html',
        controller: 'ApproveDistributerCtrl',
	
	});
}])


.controller('ApproveDistributerCtrl', ['$scope', 'CommonProp', '$firebaseArray', '$firebaseObject', '$location','distributeridservice' , function($scope, CommonProp, $firebaseArray, $firebaseObject, $location ,distributeridservice){
	$scope.username = CommonProp.getUser();

	if(!$scope.username){
		$location.path('/home');
    }
    
    var ref = firebase.database().ref().child('Distributors').orderByChild("kycDone").equalTo("yes");
	$scope.distributers = $firebaseArray(ref);	

    $scope.kycDocumentPage =function(id)
	{

        distributeridservice.set(id);
    $location.path('/kycdoc');
		// $scope.distributersid =id;
		// console.log("User distributers" + id);
        
		// var ref = firebase.database().ref().child('KYC').child(id);
		// $scope.kycdoc = $firebaseObject(ref);
		// console.log("User kyc doc" +$scope.kycdoc.qrlink );
	

    };
    
    $scope.pendingpost = function(id){
		var ref = firebase.database().ref().child('Distributors').child(id);
        $scope.pendingpost = $firebaseObject(ref);
        console.log("click on approve post - " +$scope.pendingpost.shopname );
	};


    $scope.approveKyc = function(id)
    {
        var ref = firebase.database().ref().child('Distributors').child(id);
            ref.update({
                accountStatus: "approved",
                kycDone: "yes"
            }).then(function(ref){
                $scope.$apply(function(){
                    $("#Approvmodel").modal('hide');
                });
            }, function(error){
                console.log(error);
            });
        };


        $scope.rejectKyc = function(id)
        {
            var ref = firebase.database().ref().child('Distributors').child(id);
                ref.update({
                    accountStatus: "reject",
                    kycDone: "no"
                }).then(function(ref){
                    $scope.$apply(function(){
                        $("#rejectmodel").modal('hide');
                    });
                }, function(error){
                    console.log(error);
                });
            };
    


}])