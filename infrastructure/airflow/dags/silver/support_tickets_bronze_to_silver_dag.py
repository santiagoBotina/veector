from dags.silver.factory import make_silver_dag

make_silver_dag(
    dag_id="support_tickets_bronze_to_silver",
    spark_class="com.veector.bronze_to_silver.supporttickets.Job",
    schedule="@daily",
    tags=["transformation", "support_tickets", "silver"],
)()
