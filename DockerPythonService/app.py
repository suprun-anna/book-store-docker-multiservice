from flask import Flask, request, jsonify
import mysql.connector

app = Flask(__name__)


def get_db_connection():
    return mysql.connector.connect(
        host="mysqldb",
        port=3306,
        user="root",
        password="1234567890",
        database="book_store"
    )


def execute_query(query, params=None):
    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)
    cursor.execute(query, params)
    result = cursor.fetchone()
    cursor.close()
    connection.close()
    return result


def get_orders_sum_between_dates(start_date, end_date):
    query = ("SELECT SUM(total) AS total_sum "
             "FROM orders "
             "WHERE order_date >= %s AND order_date <= %s")
    result = execute_query(query, (start_date, end_date))
    total_sum = result['total_sum'] if result['total_sum'] else 0
    return total_sum


def get_users_count():
    query = "SELECT COUNT(*) AS user_count FROM users"
    result = execute_query(query)
    user_count = result['user_count'] if result['user_count'] else 0
    return user_count


def get_books_count():
    query = "SELECT COUNT(*) AS book_count FROM books"
    result = execute_query(query)
    book_count = result['book_count'] if result['book_count'] else 0
    return book_count


def get_most_popular_book():
    query = ("SELECT book_id, SUM(quantity) AS total_quantity "
             "FROM order_items "
             "GROUP BY book_id "
             "ORDER BY total_quantity DESC "
             "LIMIT 1")
    result = execute_query(query)
    book_id = result['book_id'] if result else None
    return book_id


@app.route('/api/orders', methods=['GET'])
def get_orders():
    start_date = request.args.get('from')
    end_date = request.args.get('to')
    if start_date is None or end_date is None:
       start_date = '0001-01-01'
       end_date = '9999-12-31'
    orders_sum = get_orders_sum_between_dates(start_date, end_date)
    return jsonify({"total_sum": orders_sum})


@app.route('/api/users/count', methods=['GET'])
def get_users():
    user_count = get_users_count()
    return jsonify({"user_count": user_count})


@app.route('/api/books/count', methods=['GET'])
def get_books():
    book_count = get_books_count()
    return jsonify({"book_count": book_count})


@app.route('/api/books/popular', methods=['GET'])
def get_popular_book():
    popular_book = get_most_popular_book()
    return jsonify({"popular_book": popular_book})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8082, debug=True)
