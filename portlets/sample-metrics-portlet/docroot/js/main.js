AUI.add(
	'module-metrics',
	function(A) {
		var Lang = A.Lang;

		var NAME = 'metrics';

		var ModuleMetrics = A.Component.create(
			{
				ATTRS: {
					chartId: {
						validator: Lang.isString
					},

					frequency: {
						validator: Lang.isNumber,
						value: 1000
					},

					serviceURL: {
						validator: Lang.isString
					}
				},

				NAME: NAME,

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				prototype: {
					initializer: function(config) {
						var instance = this;

						instance._chart = new A.Chart(
							{
								dataProvider: [],
								render: '#' + instance.get('chartId'),
								type: "column"
							}
						);

						instance._frequency = instance.get('frequency');
						instance._serviceURL = instance.get('serviceURL');
					},

					start : function() {
						var instance = this;

						A.later(
							instance._frequency,
							instance,
							instance._update,
							[],
							true
						);
					},

					_update : function() {
						var instance = this;

						A.io.request(
							instance._serviceURL,
							{
								data: {
									cmd: 'list'
								},
								dataType: 'json',
								on: {
									failure: function(event, id, obj) {
										console.log("failure");
									},

									success: function(event, id, obj) {
										var results = this.get('responseData');

										console.log(results);

										// we need to update the datasource of the chart

										instance._chart.set('dataProvider', results);
									}
								}
							}
						);
					}
				}
			}
		);

		Liferay.ModuleMetrics = ModuleMetrics;
	},
	'',
	{
		requires: ['aui-base', 'aui-io-request', 'charts', 'liferay-portlet-base']
	}
);
