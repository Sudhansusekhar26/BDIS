/**
 * BDIS - Dashboard & Pipeline Interactivity
 */
document.addEventListener('DOMContentLoaded', function () {
    // 1. Dispatch Modal Prefill
    const dispatchModal = document.getElementById('dispatchModal');
    if (dispatchModal) {
        dispatchModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const requestId = button.getAttribute('data-request-id');
            const patientName = button.getAttribute('data-patient-name');
            const units = button.getAttribute('data-units');
            const bloodGroup = button.getAttribute('data-blood-group');
            const address = button.getAttribute('data-address');

            const form = document.getElementById('dispatchForm');
            form.action = `/hospital/requests/${requestId}/dispatch`;

            document.getElementById('modalReqId').textContent = '#' + requestId;
            document.getElementById('modalPatientName').textContent = patientName;
            document.getElementById('modalUnitsGroup').textContent = `${units} Units of ${bloodGroup}`;
            document.getElementById('modalAddress').textContent = address;
        });
    }

    // 2. Reject Modal Prefill
    const rejectModal = document.getElementById('rejectModal');
    if (rejectModal) {
        rejectModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const requestId = button.getAttribute('data-request-id');
            const form = document.getElementById('rejectForm');
            form.action = `/hospital/requests/${requestId}/reject`;
            document.getElementById('rejectModalReqId').textContent = '#' + requestId;
        });
    }

    // 3. Admin Override Modal Prefill
    const overrideModal = document.getElementById('overrideModal');
    if (overrideModal) {
        overrideModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const inventoryId = button.getAttribute('data-inventory-id');
            const hospitalName = button.getAttribute('data-hospital-name');
            const bloodGroup = button.getAttribute('data-blood-group');
            const currentUnits = button.getAttribute('data-units');

            const form = document.getElementById('overrideForm');
            form.action = `/admin/inventory/${inventoryId}/override`;

            document.getElementById('overrideHospitalName').textContent = hospitalName;
            document.getElementById('overrideBloodGroup').textContent = bloodGroup;
            document.getElementById('overrideCurrentUnits').value = currentUnits;
        });
    }
});
