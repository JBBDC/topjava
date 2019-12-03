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
    $('.checkbox').change(function () {
        let id = $(this).closest('tr').prop('id');
        setEnable(this.checked, id);
    });
});

function setEnable(isEnabled, id) {
    $.ajax({
        url: context.ajaxUrl + "enable",
        method: "POST",
        data: {
            isEnabled: isEnabled,
            id: id
        },
    }).done(function () {
        successNoty(isEnabled ? "Enabled" : "Disabled");
        $("#"+id).attr("data-enabled", isEnabled);
    });
}