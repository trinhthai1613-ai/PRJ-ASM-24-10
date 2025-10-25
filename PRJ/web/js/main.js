/**
 * LEAVE MANAGEMENT SYSTEM - MAIN JAVASCRIPT
 * Xử lý các chức năng client-side
 */

// ============================================
// UTILS
// ============================================

/**
 * Show alert message
 */
function showAlert(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    
    const container = document.querySelector('.container') || document.body;
    container.insertBefore(alertDiv, container.firstChild);
    
    // Auto hide after 5 seconds
    setTimeout(() => {
        alertDiv.style.opacity = '0';
        setTimeout(() => alertDiv.remove(), 300);
    }, 5000);
}

/**
 * Confirm dialog
 */
function confirmAction(message) {
    return confirm(message);
}

/**
 * Format date to DD/MM/YYYY
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

/**
 * Calculate working days between two dates
 */
function calculateWorkingDays(fromDate, toDate) {
    const start = new Date(fromDate);
    const end = new Date(toDate);
    let count = 0;
    
    const current = new Date(start);
    while (current <= end) {
        const dayOfWeek = current.getDay();
        if (dayOfWeek !== 0 && dayOfWeek !== 6) { // Not Sunday (0) or Saturday (6)
            count++;
        }
        current.setDate(current.getDate() + 1);
    }
    
    return count;
}

// ============================================
// FORM VALIDATION
// ============================================

/**
 * Validate leave request form
 */
function validateLeaveRequestForm() {
    const form = document.getElementById('leaveRequestForm');
    if (!form) return true;
    
    const fromDate = document.getElementById('fromDate').value;
    const toDate = document.getElementById('toDate').value;
    
    if (!fromDate || !toDate) {
        showAlert('Vui lòng chọn ngày bắt đầu và ngày kết thúc', 'error');
        return false;
    }
    
    if (new Date(fromDate) > new Date(toDate)) {
        showAlert('Ngày bắt đầu phải trước hoặc bằng ngày kết thúc', 'error');
        return false;
    }
    
    // Calculate and display working days
    const workingDays = calculateWorkingDays(fromDate, toDate);
    const confirmation = confirm(`Bạn sẽ nghỉ ${workingDays} ngày làm việc. Xác nhận?`);
    
    return confirmation;
}

/**
 * Validate login form
 */
function validateLoginForm() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    
    if (!username) {
        showAlert('Vui lòng nhập username', 'error');
        return false;
    }
    
    if (!password) {
        showAlert('Vui lòng nhập password', 'error');
        return false;
    }
    
    if (password.length < 6) {
        showAlert('Password phải có ít nhất 6 ký tự', 'error');
        return false;
    }
    
    return true;
}

// ============================================
// AUTO-CALCULATE DAYS
// ============================================

/**
 * Auto calculate working days when dates change
 */
function setupDateCalculation() {
    const fromDateInput = document.getElementById('fromDate');
    const toDateInput = document.getElementById('toDate');
    const totalDaysDisplay = document.getElementById('totalDaysDisplay');
    
    if (!fromDateInput || !toDateInput) return;
    
    function updateTotalDays() {
        const fromDate = fromDateInput.value;
        const toDate = toDateInput.value;
        
        if (fromDate && toDate) {
            const workingDays = calculateWorkingDays(fromDate, toDate);
            if (totalDaysDisplay) {
                totalDaysDisplay.textContent = `Số ngày làm việc: ${workingDays}`;
                totalDaysDisplay.style.color = workingDays > 0 ? '#27ae60' : '#e74c3c';
            }
        }
    }
    
    fromDateInput.addEventListener('change', updateTotalDays);
    toDateInput.addEventListener('change', updateTotalDays);
}

// ============================================
// TABLE ACTIONS
// ============================================

/**
 * Confirm delete action
 */
function confirmDelete(itemName) {
    return confirm(`Bạn có chắc chắn muốn xóa "${itemName}"?`);
}

/**
 * Confirm approve action
 */
function confirmApprove() {
    return confirm('Bạn có chắc chắn muốn DUYỆT đơn này?');
}

/**
 * Confirm reject action
 */
function confirmReject() {
    const note = prompt('Lý do từ chối (tùy chọn):');
    if (note === null) return false; // User clicked Cancel
    
    // Set note value to hidden input if exists
    const noteInput = document.getElementById('processNote');
    if (noteInput) {
        noteInput.value = note;
    }
    
    return true;
}

// ============================================
// TABLE SEARCH & FILTER
// ============================================

/**
 * Search in table
 */
function searchTable(inputId, tableId) {
    const input = document.getElementById(inputId);
    const table = document.getElementById(tableId);
    
    if (!input || !table) return;
    
    input.addEventListener('keyup', function() {
        const filter = this.value.toUpperCase();
        const rows = table.getElementsByTagName('tr');
        
        for (let i = 1; i < rows.length; i++) { // Skip header row
            const row = rows[i];
            const cells = row.getElementsByTagName('td');
            let found = false;
            
            for (let j = 0; j < cells.length; j++) {
                const cell = cells[j];
                if (cell) {
                    const textValue = cell.textContent || cell.innerText;
                    if (textValue.toUpperCase().indexOf(filter) > -1) {
                        found = true;
                        break;
                    }
                }
            }
            
            row.style.display = found ? '' : 'none';
        }
    });
}

/**
 * Filter table by status
 */
function filterByStatus(selectId, tableId) {
    const select = document.getElementById(selectId);
    const table = document.getElementById(tableId);
    
    if (!select || !table) return;
    
    select.addEventListener('change', function() {
        const filterValue = this.value.toUpperCase();
        const rows = table.getElementsByTagName('tr');
        
        for (let i = 1; i < rows.length; i++) {
            const row = rows[i];
            const statusCell = row.querySelector('.status-badge');
            
            if (!statusCell) continue;
            
            const statusText = statusCell.textContent.toUpperCase();
            
            if (filterValue === '' || statusText.indexOf(filterValue) > -1) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        }
    });
}

// ============================================
// LOADING SPINNER
// ============================================

/**
 * Show loading spinner
 */
function showLoading() {
    const spinner = document.createElement('div');
    spinner.id = 'loadingSpinner';
    spinner.className = 'spinner';
    spinner.style.position = 'fixed';
    spinner.style.top = '50%';
    spinner.style.left = '50%';
    spinner.style.transform = 'translate(-50%, -50%)';
    spinner.style.zIndex = '9999';
    
    const overlay = document.createElement('div');
    overlay.id = 'loadingOverlay';
    overlay.style.position = 'fixed';
    overlay.style.top = '0';
    overlay.style.left = '0';
    overlay.style.width = '100%';
    overlay.style.height = '100%';
    overlay.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    overlay.style.zIndex = '9998';
    
    document.body.appendChild(overlay);
    document.body.appendChild(spinner);
}

/**
 * Hide loading spinner
 */
function hideLoading() {
    const spinner = document.getElementById('loadingSpinner');
    const overlay = document.getElementById('loadingOverlay');
    
    if (spinner) spinner.remove();
    if (overlay) overlay.remove();
}

// ============================================
// FORM SUBMIT WITH LOADING
// ============================================

/**
 * Setup form with loading indicator
 */
function setupFormWithLoading(formId) {
    const form = document.getElementById(formId);
    if (!form) return;
    
    form.addEventListener('submit', function() {
        showLoading();
        // Hide after 30 seconds max (in case of error)
        setTimeout(hideLoading, 30000);
    });
}

// ============================================
// DATE PICKER RESTRICTIONS
// ============================================

/**
 * Set minimum date for date inputs (today)
 */
function setMinDateToday() {
    const dateInputs = document.querySelectorAll('input[type="date"]');
    const today = new Date().toISOString().split('T')[0];
    
    dateInputs.forEach(input => {
        if (!input.hasAttribute('data-allow-past')) {
            input.setAttribute('min', today);
        }
    });
}

// ============================================
// TOOLTIP
// ============================================

/**
 * Initialize tooltips
 */
function initTooltips() {
    const tooltipElements = document.querySelectorAll('[data-tooltip]');
    
    tooltipElements.forEach(element => {
        element.addEventListener('mouseenter', function() {
            const tooltipText = this.getAttribute('data-tooltip');
            const tooltip = document.createElement('div');
            tooltip.className = 'tooltip';
            tooltip.textContent = tooltipText;
            tooltip.style.position = 'absolute';
            tooltip.style.backgroundColor = '#333';
            tooltip.style.color = 'white';
            tooltip.style.padding = '5px 10px';
            tooltip.style.borderRadius = '4px';
            tooltip.style.fontSize = '0.85rem';
            tooltip.style.zIndex = '1000';
            
            document.body.appendChild(tooltip);
            
            const rect = this.getBoundingClientRect();
            tooltip.style.top = (rect.top - tooltip.offsetHeight - 5) + 'px';
            tooltip.style.left = (rect.left + (rect.width / 2) - (tooltip.offsetWidth / 2)) + 'px';
            
            this.tooltipElement = tooltip;
        });
        
        element.addEventListener('mouseleave', function() {
            if (this.tooltipElement) {
                this.tooltipElement.remove();
            }
        });
    });
}

// ============================================
// PRINT FUNCTION
// ============================================

/**
 * Print current page
 */
function printPage() {
    window.print();
}

// ============================================
// BACK BUTTON
// ============================================

/**
 * Go back to previous page
 */
function goBack() {
    if (window.history.length > 1) {
        window.history.back();
    } else {
        window.location.href = document.referrer || '/';
    }
}

// ============================================
// INITIALIZATION
// ============================================

/**
 * Initialize all functions when DOM is ready
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Leave Management System - Client JS Loaded');
    
    // Setup date calculation
    setupDateCalculation();
    
    // Set minimum date to today
    setMinDateToday();
    
    // Initialize tooltips
    initTooltips();
    
    // Setup search if search input exists
    if (document.getElementById('searchInput') && document.getElementById('dataTable')) {
        searchTable('searchInput', 'dataTable');
    }
    
    // Setup status filter if exists
    if (document.getElementById('statusFilter') && document.getElementById('dataTable')) {
        filterByStatus('statusFilter', 'dataTable');
    }
    
    // Setup form with loading for main forms
    setupFormWithLoading('leaveRequestForm');
    setupFormWithLoading('processRequestForm');
    
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
});

// ============================================
// EXPORT FUNCTIONS FOR GLOBAL USE
// ============================================

window.LeaveMS = {
    showAlert,
    confirmAction,
    confirmApprove,
    confirmReject,
    confirmDelete,
    formatDate,
    calculateWorkingDays,
    validateLeaveRequestForm,
    validateLoginForm,
    printPage,
    goBack,
    showLoading,
    hideLoading
};