$(function () {
		
			
		
			var $activate_selectator1 = $('#activate_selectator1');
			$activate_selectator1.click(function () {
				var $select1 = $('#select1');
				if ($select1.data('selectator') === undefined) {
					$select1.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator1.val('destroy selectator');
				} else {
					$select1.selectator('destroy');
					$activate_selectator1.val('activate selectator');
				}
			});
			$activate_selectator1.trigger('click');

			
			var $activate_selectator2 = $('#activate_selectator2');
			$activate_selectator2.click(function () {
				var $select2 = $('#select2');
				if ($select2.data('selectator') === undefined) {
					$select2.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator2.val('destroy selectator');
				} else {
					$select2.selectator('destroy');
					$activate_selectator2.val('activate selectator');
				}
			});
			$activate_selectator2.trigger('click');

			
			var $activate_selectator3 = $('#activate_selectator3');
			$activate_selectator3.click(function () {
				var $selectAllie1 = $('#selectAllie1');
				if ($selectAllie1.data('selectator') === undefined) {
					$selectAllie1.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator3.val('destroy selectator');
				} else {
					$selectAllie1.selectator('destroy');
					$activate_selectator3.val('activate selectator');
				}
			});
			$activate_selectator3.trigger('click');
			
			
			var $activate_selectator4 = $('#activate_selectator4');
			$activate_selectator4.click(function () {
				var $selectAllie2 = $('#selectAllie2');
				if ($selectAllie2.data('selectator') === undefined) {
					$selectAllie2.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator4.val('destroy selectator');
				} else {
					$selectAllie2.selectator('destroy');
					$activate_selectator4.val('activate selectator');
				}
			});
			$activate_selectator4.trigger('click');
			
			
			var $activate_selectator5 = $('#activate_selectator5');
			$activate_selectator5.click(function () {
				var $selectAllie3 = $('#selectAllie3');
				if ($selectAllie3.data('selectator') === undefined) {
					$selectAllie3.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator5.val('destroy selectator');
				} else {
					$selectAllie3.selectator('destroy');
					$activate_selectator5.val('activate selectator');
				}
			});
			$activate_selectator5.trigger('click');
			
			
			var $activate_selectator6 = $('#activate_selectator6');
			$activate_selectator6.click(function () {
				var $selectAllie4 = $('#selectAllie4');
				if ($selectAllie4.data('selectator') === undefined) {
					$selectAllie4.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator6.val('destroy selectator');
				} else {
					$selectAllie4.selectator('destroy');
					$activate_selectator6.val('activate selectator');
				}
			});
			$activate_selectator6.trigger('click');
			
			
			
			var $activate_selectator7 = $('#activate_selectator7');
			$activate_selectator7.click(function () {
				var $selectEnemy1 = $('#selectEnemy1');
				if ($selectEnemy1.data('selectator') === undefined) {
					$selectEnemy1.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator7.val('destroy selectator');
				} else {
					$selectEnemy1.selectator('destroy');
					$activate_selectator7.val('activate selectator');
				}
			});
			$activate_selectator7.trigger('click');
			
			var $activate_selectator8 = $('#activate_selectator8');
			$activate_selectator8.click(function () {
				var $selectEnemy2 = $('#selectEnemy2');
				if ($selectEnemy2.data('selectator') === undefined) {
					$selectEnemy2.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator8.val('destroy selectator');
				} else {
					$selectEnemy2.selectator('destroy');
					$activate_selectator8.val('activate selectator');
				}
			});
			$activate_selectator8.trigger('click');
			
			
			var $activate_selectator9 = $('#activate_selectator9');
			$activate_selectator9.click(function () {
				var $selectEnemy3 = $('#selectEnemy3');
				if ($selectEnemy3.data('selectator') === undefined) {
					$selectEnemy3.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator9.val('destroy selectator');
				} else {
					$selectEnemy3.selectator('destroy');
					$activate_selectator9.val('activate selectator');
				}
			});
			$activate_selectator9.trigger('click');
			
			
			var $activate_selectator10 = $('#activate_selectator10');
			$activate_selectator10.click(function () {
				var $selectEnemy4 = $('#selectEnemy4');
				if ($selectEnemy4.data('selectator') === undefined) {
					$selectEnemy4.selectator({
						labels: {
							search: 'Search here...'
						}
					});
					$activate_selectator10.val('destroy selectator');
				} else {
					$selectEnemy4.selectator('destroy');
					$activate_selectator10.val('activate selectator');
				}
			});
			$activate_selectator10.trigger('click');
		});