from dags.silver.factory import make_silver_dag

make_silver_dag(
    dag_id="marketing_events_bronze_to_silver",
    spark_class="com.veector.bronze_to_silver.marketingevents.Job",
    schedule="@daily",
    tags=["transformation", "marketing_events", "silver"],
)()
