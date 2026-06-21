const CAR_API      = 'http://localhost:8080/api';
const CUSTOMER_API = 'http://localhost:8081/api';

// ── Tab switching ─────────────────────────────────────────────────────────────

function showTab(tab, btn) {
    document.querySelectorAll('.tab-section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    document.getElementById(tab + '-section').classList.add('active');
    btn.classList.add('active');
}

// ── Cars ──────────────────────────────────────────────────────────────────────

async function loadCars() {
    try {
        const res = await fetch(`${CAR_API}/cars`);
        if (!res.ok) throw new Error();
        const cars = await res.json();
        const tbody = document.getElementById('cars-tbody');
        if (cars.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="loading">No cars registered.</td></tr>';
        } else {
            tbody.innerHTML = cars.map(car => `
                <tr>
                    <td><strong>${car.plateNumber}</strong></td>
                    <td>${car.brand}</td>
                    <td>€${car.price.toFixed(2)}</td>
                    <td>
                        <span class="badge ${car.rented ? 'badge-rented' : 'badge-available'}">
                            ${car.rented ? 'Rented' : 'Available'}
                        </span>
                    </td>
                    <td>
                        ${!car.rented
                            ? `<button class="btn btn-primary" onclick="rentCar('${car.plateNumber}')">Rent</button>`
                            : `<button class="btn btn-warning" onclick="returnCar('${car.plateNumber}')">Return</button>`
                        }
                        <button class="btn btn-danger" onclick="deleteCar('${car.plateNumber}')">Delete</button>
                    </td>
                </tr>
            `).join('');
        }
        hideError('cars');
    } catch {
        showError('cars', 'Cannot connect to Car Service (localhost:8080). Make sure it is running.');
    }
}

async function rentCar(plate) {
    const res = await fetch(`${CAR_API}/cars/${plate}/rent`, { method: 'POST' });
    if (res.ok) loadCars();
    else showError('cars', `Could not rent car ${plate}.`);
}

async function returnCar(plate) {
    const res = await fetch(`${CAR_API}/cars/${plate}/return`, { method: 'POST' });
    if (res.ok) loadCars();
    else showError('cars', `Could not return car ${plate}.`);
}

async function deleteCar(plate) {
    if (!confirm(`Delete car ${plate}?`)) return;
    const res = await fetch(`${CAR_API}/cars/${plate}`, { method: 'DELETE' });
    if (res.ok) loadCars();
    else showError('cars', `Could not delete car ${plate}.`);
}

async function addCar(event) {
    event.preventDefault();
    const car = {
        plateNumber: document.getElementById('car-plate').value.trim(),
        brand:       document.getElementById('car-brand').value.trim(),
        price:       parseFloat(document.getElementById('car-price').value),
        rented:      false
    };
    const res = await fetch(`${CAR_API}/cars`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(car)
    });
    if (res.ok) {
        document.getElementById('add-car-form').reset();
        loadCars();
    } else {
        showError('cars', 'Could not add car. Check the form and try again.');
    }
}

// ── Customers ─────────────────────────────────────────────────────────────────

async function loadCustomers() {
    try {
        const res = await fetch(`${CUSTOMER_API}/customers`);
        if (!res.ok) throw new Error();
        const customers = await res.json();
        const tbody = document.getElementById('customers-tbody');
        if (customers.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="loading">No customers registered.</td></tr>';
        } else {
            tbody.innerHTML = customers.map(c => `
                <tr>
                    <td>${c.id}</td>
                    <td>${c.firstName}</td>
                    <td>${c.lastName}</td>
                    <td>${c.email}</td>
                    <td>
                        <button class="btn btn-danger" onclick="deleteCustomer(${c.id})">Delete</button>
                    </td>
                </tr>
            `).join('');
        }
        hideError('customers');
    } catch {
        showError('customers', 'Cannot connect to Customer Service (localhost:8081). Make sure it is running.');
    }
}

async function addCustomer(event) {
    event.preventDefault();
    const customer = {
        firstName: document.getElementById('customer-firstname').value.trim(),
        lastName:  document.getElementById('customer-lastname').value.trim(),
        email:     document.getElementById('customer-email').value.trim()
    };
    const res = await fetch(`${CUSTOMER_API}/customers`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(customer)
    });
    if (res.ok) {
        document.getElementById('add-customer-form').reset();
        loadCustomers();
    } else {
        showError('customers', 'Could not add customer. The email may already be in use.');
    }
}

async function deleteCustomer(id) {
    if (!confirm(`Delete customer #${id}?`)) return;
    const res = await fetch(`${CUSTOMER_API}/customers/${id}`, { method: 'DELETE' });
    if (res.ok) loadCustomers();
    else showError('customers', `Could not delete customer #${id}.`);
}

// ── Helpers ───────────────────────────────────────────────────────────────────

function showError(section, msg) {
    const el = document.getElementById(section + '-error');
    el.textContent = msg;
    el.classList.remove('hidden');
}

function hideError(section) {
    document.getElementById(section + '-error').classList.add('hidden');
}

// ── Init ──────────────────────────────────────────────────────────────────────

loadCars();
loadCustomers();
