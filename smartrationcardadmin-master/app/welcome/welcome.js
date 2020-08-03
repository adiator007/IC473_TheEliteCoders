'use strict';

angular.module('webApp.welcome', ['ngRoute', 'firebase'])

.config(['$routeProvider', function($routeProvider){
	$routeProvider.when('/welcome',{
		templateUrl: 'welcome/welcome.html',
		controller: 'WelcomeCtrl',
		styleUrls: 'welcome/component.css'
	});
}])


.controller('WelcomeCtrl', ['$scope', 'CommonProp', '$firebaseArray', '$firebaseObject', '$location', function($scope, CommonProp, $firebaseArray, $firebaseObject, $location){
	$scope.username = CommonProp.getUser();

	if(!$scope.username){
		$location.path('/home');
	}

	var ref = firebase.database().ref().child('Distributors');
	$scope.distributers = $firebaseArray(ref);
	
	var approvedDistributerRef = firebase.database().ref().child('Distributors').orderByChild("kycDone").equalTo("yes");
	$scope.countapproveddistributer = $firebaseArray(approvedDistributerRef);

	var approvedConsumerRef = firebase.database().ref().child('Customers').orderByChild("kycDone").equalTo("yes");
	$scope.countapprovedconsumers = $firebaseArray(approvedConsumerRef);



	// var ref = firebase.database().ref().child('Distributors');
	// $scope.Totaldistributers = $firebaseArray(ref);
	

	var Customerref = firebase.database().ref().child('Customers');
	$scope.TotalCustomers = $firebaseArray(Customerref);
	
	$scope.distributers.$loaded().then(function(distributers) {


		// $scope.TotalCustomers.$loaded().then(function(TotalCustomers){

		// 	$scope.countapproveddistributer.$loaded().then(function(countapproveddistributer){

		// 		$scope.countapprovedconsumersr.$loaded().then(function(countapprovedconsumersr){

					var TotaltaskProgress = TotalCustomers.length + distributers.length ;
					var perTaskProgress =countapproveddistributer.length +countapprovedconsumersr.length ;
				
					$scope.Taskremain =( TotaltaskProgress / perTaskProgress ) * 100 ;

					var e1 = document.getElementById("progress_bar");
						e1.style.width = "50px";
						$(".progress-bar").css('width', $scope.Taskremain+'70%');
					//  $('#progress_bar').addRule('width',$scope.Taskremain+ "%");
					console.log($scope.Taskremain); 
		// 		});
	

		// 	s
		// 	});



		// });
		

		// console.log(distributers.length); 
	 });


	// console.log("Total Customers" +$scope.distributers.length );
	// console.log("TotalProgress" +TotaltaskProgresss);
	// console.log("TaskPercentage" +$scope.TaskRemain);

	$scope.editPost = function(id){
		var ref = firebase.database().ref().child('Articles/' + id);
		$scope.editPostData = $firebaseObject(ref);
	};

	$scope.viewDistributers =function()

	{
		$location.path('/distributers');
	}

	$scope.viewConsumers =function()

	{
		$location.path('/consumers');
	}


	$scope.kycDocumentPage =function(id)
	{
		$scope.distributersid =id;
		console.log("User distributers" + id);

		var ref = firebase.database().ref().child('KYC').child(id);
		$scope.kycdoc = $firebaseObject(ref);
		console.log("User kyc doc" +$scope.kycdoc.qrlink );
	

	};
	$scope.updatePost = function(id){
		var ref = firebase.database().ref().child('Articles/' + id);
		ref.update({
			title: $scope.editPostData.title,
			post: $scope.editPostData.post
		}).then(function(ref){
			$scope.$apply(function(){
				$("#editModal").modal('hide');
			});
		}, function(error){
			console.log(error);
		});
	};

	$scope.deleteCnf = function(article){
		$scope.deleteArticle = article;
	};

	$scope.deletePost = function(deleteArticle){
		$scope.articles.$remove(deleteArticle);
		$("#deleteModal").modal('hide');
	};

	$scope.logout = function(){
		CommonProp.logoutUser();
		
	}
}])