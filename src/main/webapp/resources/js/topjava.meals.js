$(function () {
    function getFiltered() {
        let startDate = document.getElementById("startDate").value;
        let startTime = document.getElementById("startTime").value;
        let endDate = document.getElementById("endDate").value;
        let endTime = document.getElementById("endTime").value;
        $.ajax({
            url: "ajax/meals/filter",
            method: "GET",
            data: {
                startDate: startDate,
                startTime: startTime,
                endDate: endDate,
                endTime: endTime
            },
            dataType: "json"
        }).done(function (data) {
            context.datatableApi.clear().rows.add(data).draw();
            successNoty("Filtered")
        });
    }

    $("#filter-meals-button").click(getFiltered);

    $("#reset-meals-button").click(function () {
        document.getElementById("filter").reset();
        updateTable();
    });

    $("#startDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
    })
    $("#endDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $("#startTime ").datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
    $("#endTime").datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
    $("#dateTime").datetimepicker({format: 'Y-m-d H:i'});

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


