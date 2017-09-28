function wrapTimepickerToInput(input) {
	input.timepicker({
		timeFormat: "H:i",
		step: 15,
		disableTextInput: true,
		defaultTime: "8"
	});
}

function wrapDatepickerToInput(input) {
	input.datepicker({
		dateFormat: "dd.mm.yy.",
		changeMonth: true,
		changeYeat: true
	});
	input.keyup(function() {
		$(this).val("");
	});
}

function onAjaxEdit(data) {
	if (data.status === "success") {
		wrapTimepickerToInput($("#form\\:working-hour-start"));
		wrapTimepickerToInput($("#form\\:working-hour-end"));
		$("#form\\:salary-amount").keyup(function() {
			var value = $(this).val().toString();
			var lastChar = value.slice(value.length - 1);
			if (lastChar === ".") {
				if ((value.match(/\./g) || []).length > 1) {
					$(this).val(value.replace(".", ""));
				}
			} else {
				if (!/[0-9]/.test(lastChar)) {
					$(this).val(value.slice(0, value.length - 1));
				}
			}
		});
		$("#form\\:salary-amount").blur(function() {
			var value = $(this).val().toString();
			var lastChar = value.slice(value.length - 1);
			if (lastChar === ".") {
				$(this).val(value + "00");
			}
		});
	}
}

function onLicensePanelLoad() {
	wrapDatepickerToInput($("#form\\:acquisition-date"));
}

function onTrainingPanelLoad() {
	wrapDatepickerToInput($("#form\\:start-date"));
	wrapDatepickerToInput($("#form\\:end-date"));
}