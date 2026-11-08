/**
 * BDIS - Client-side Interactive Logic & Location Hierarchy Cascade
 */
document.addEventListener('DOMContentLoaded', function () {
    // 1. Initialize State -> District Dependent Dropdowns
    const stateSelects = document.querySelectorAll('.state-select-cascade');
    stateSelects.forEach(function (stateSelect) {
        const targetDistrictSelector = stateSelect.getAttribute('data-district-target');
        const districtSelect = document.querySelector(targetDistrictSelector);

        if (stateSelect && districtSelect) {
            stateSelect.addEventListener('change', function () {
                const stateId = this.value;
                if (!stateId) {
                    districtSelect.innerHTML = '<option value="">-- Select District --</option>';
                    districtSelect.disabled = true;
                    return;
                }
                loadDistrictsForState(stateId, districtSelect);
            });

            // Initial load if state already selected (e.g. on search or validation reload)
            if (stateSelect.value) {
                const preselectedDistrict = districtSelect.getAttribute('data-preselected') || districtSelect.value;
                loadDistrictsForState(stateSelect.value, districtSelect, preselectedDistrict);
            }
        }
    });

    // 2. Fetch and populate districts
    function loadDistrictsForState(stateId, districtSelectElement, preselectedVal) {
        districtSelectElement.disabled = true;
        districtSelectElement.innerHTML = '<option value="">Loading districts...</option>';

        fetch(`/api/locations/districts?stateId=${encodeURIComponent(stateId)}`)
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch districts');
                return response.json();
            })
            .then(json => {
                if (json.success && Array.isArray(json.data)) {
                    let html = '<option value="">-- Select District --</option>';
                    json.data.forEach(district => {
                        const isSelected = (preselectedVal && String(preselectedVal) === String(district.id)) ? 'selected' : '';
                        html += `<option value="${district.id}" ${isSelected}>${district.name}</option>`;
                    });
                    districtSelectElement.innerHTML = html;
                    districtSelectElement.disabled = false;
                } else {
                    districtSelectElement.innerHTML = '<option value="">No districts found</option>';
                }
            })
            .catch(err => {
                console.error('Error loading districts:', err);
                districtSelectElement.innerHTML = '<option value="">Error loading districts</option>';
            });
    }

    // 3. Live Client-Side Table Filter for Inventory
    const liveFilterInput = document.getElementById('liveTableFilter');
    if (liveFilterInput) {
        liveFilterInput.addEventListener('keyup', function () {
            const query = this.value.toLowerCase().trim();
            const tableRows = document.querySelectorAll('#inventoryTable tbody tr');
            tableRows.forEach(row => {
                const text = row.innerText.toLowerCase();
                row.style.display = text.includes(query) ? '' : 'none';
            });
        });
    }

    // 4. Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 5000);
    });

    // 5. Tooltips and Popovers
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});

/**
 * Toast Notification Helper
 */
function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) return;

    const toastId = 'toast_' + Date.now();
    const bgClass = type === 'success' ? 'bg-success text-white' :
                    type === 'danger' ? 'bg-danger text-white' : 'bg-primary text-white';

    const toastHtml = `
        <div id="${toastId}" class="toast align-items-center ${bgClass} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-info-circle-fill me-2"></i> ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, { delay: 4000 });
    toast.show();

    toastElement.addEventListener('hidden.bs.toast', function () {
        toastElement.remove();
    });
}
