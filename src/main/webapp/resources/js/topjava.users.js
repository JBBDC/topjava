$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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

function enable(id) {
    $.ajax({
        url: context.ajaxUrl + "enable/" + id,
        method: "PUT"
    }).done(function () {
        updateTable();
        successNoty("Enabled");
    });
}

function disable(id) {
    $.ajax({
        url: context.ajaxUrl + "disable/" + id,
        method: "PUT"
    }).done(function () {
        updateTable();
        successNoty("Disabled");
    });
}