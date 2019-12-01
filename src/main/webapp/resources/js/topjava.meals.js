$(function () {
    $("#startDate ").datetimepicker({timepicker:false,
        format:'d.m.Y'});
    $("#endDate").datetimepicker({timepicker:false,
        format:'d.m.Y'});
    $("#startTime ").datetimepicker({datepicker:false,
        format:'H:i'});
    $("#endTime").datetimepicker({datepicker:false,
        format:'H:i'});
    $("#dateTime").datetimepicker({format:'Y-m-d H:i'});
});

$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );
});

