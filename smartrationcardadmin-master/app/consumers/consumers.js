
'use strict';

angular.module('webApp.consumers', ['ngRoute', 'firebase'])

.config(['$routeProvider', function($routeProvider){
	$routeProvider.when('/consumers',{
		templateUrl: 'consumers/consumers.html',
		controller: 'CustomersCtrl',
	
	});
}])



.controller('CustomersCtrl', ['$scope', 'CommonProp', '$firebaseArray', '$firebaseObject', '$location','distributeridservice', function($scope, CommonProp, $firebaseArray, $firebaseObject, $location ,distributeridservice){
	$scope.username = CommonProp.getUser();

	if(!$scope.username){
		$location.path('/home');
    }
    
    var ref = firebase.database().ref().child('Customers').orderByChild("kycDone").equalTo("no");
    $scope.customers = $firebaseArray(ref);	
    


    $scope.profilepicfun =function(id)
	{
    
    var ProfilePicRef = firebase.database().ref().child('CustomerKYC').child(id).child('profilepic');
    $scope.profilepic = $firebaseObject(ProfilePicRef);	
    console.log("pofilepic + id " +$scope.profilepic.url + id);

    };

    // $scope.kycDocumentPage =function(id)
	// {
	// 	$scope.customersrsid =id;
	// 	console.log("Consumer" + id);
    //     var ref = firebase.database().ref().child('CustomerKYC').child(id);
	// 	$scope.kycdoc = $firebaseObject(ref);
	// 	console.log("User kyc doc" +$scope.kycdoc.qrlink );
	

    // };



    $scope.pendingpost = function(id){
		var ref = firebase.database().ref().child('Customers').child(id);
        $scope.pendingpost = $firebaseObject(ref);
        console.log("click on approve post - " +$scope.pendingpost.shopname );
	};


    $scope.kycDocumentPage =function(id)
	{

        distributeridservice.set(id);
    $location.path('/kycdocconsumer');
		// $scope.distributersid =id;
		// console.log("User distributers" + id);
        
		// var ref = firebase.database().ref().child('KYC').child(id);
		// $scope.kycdoc = $firebaseObject(ref);
		// console.log("User kyc doc" +$scope.kycdoc.qrlink );
	

    };
    $scope.approveKyc = function(id)
    {
        var ref = firebase.database().ref().child('Customers').child(id);
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
        var ref = firebase.database().ref().child('Customers').child(id);
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