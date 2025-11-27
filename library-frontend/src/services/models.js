export class User {
    constructor(userId, phoneNumber, userName) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }
}

export class LoginRequest {
    constructor(phoneNumber, password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}

export class BorrowRequest {
    constructor(userId, inventoryId) {
        this.userId = userId;
        this.inventoryId = inventoryId;
    }
}