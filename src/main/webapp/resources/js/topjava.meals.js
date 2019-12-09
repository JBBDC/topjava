var userAjaxUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "ajax/profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/profile/meals/", updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: userAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": userAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.replace(/T/g, " ");
                        }
                        return data;
                    }
                },
                {
                    "data": "description",
                    "render": function (data, type, row) {
                        return data;
                    }
                },
                {
                    "data": "calories",
                    "render": function (data, type, row) {
                        return data;
                    }
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "rowCallback": function (row, data, index) {
                $(row).attr('data-mealExcess', data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });

    $("#startDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                maxDate: jQuery('#endDate').val() ? jQuery('#endDate').val() : false
            })
        }
    });
    $("#endDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                minDate: jQuery('#startDate').val() ? jQuery('#startDate').val() : false
            })
        }
    });
    $("#startTime ").datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                maxTime: jQuery('#endTime').val() ? jQuery('#endTime').val() : false
            })
        }
    });
    $("#endTime").datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                minTime: jQuery('#startTime').val() ? jQuery('#startTime').val() : false
            })
        }
    });
    $("#dateTime").datetimepicker({format: 'Y-m-d H:i'});
});