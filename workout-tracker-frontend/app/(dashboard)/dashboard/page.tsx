'use client';

import { useState, useEffect, useCallback } from 'react';
import { workoutPlansApi, WorkoutPlan } from '@/lib/api';
import { Button } from '@/components/ui/button';
import { Plus, Trash2, Loader2, Dumbbell } from 'lucide-react';
import WorkoutPlanCard from '@/components/workout-plan-card';
import WorkoutPlanModal from '@/components/workout-plan-modal';
import DeleteConfirmModal from '@/components/delete-confirm-modal';
import { toast } from 'sonner';

export default function DashboardPage() {
  const [plans, setPlans] = useState<WorkoutPlan[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingPlan, setEditingPlan] = useState<WorkoutPlan | null>(null);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [deletingPlan, setDeletingPlan] = useState<WorkoutPlan | null>(null);
  const [deleteAllModalOpen, setDeleteAllModalOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  const fetchPlans = useCallback(async () => {
    try {
      const response = await workoutPlansApi.getAll();
      setPlans(response.data);
    } catch {
      // Error handled by interceptor
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchPlans();
  }, [fetchPlans]);

  const handleCreateOrUpdate = async (data: { name: string; description?: string }) => {
    if (editingPlan) {
      await workoutPlansApi.update(editingPlan.id, data);
      toast.success('Updated successfully');
    } else {
      await workoutPlansApi.create(data);
      toast.success('Created successfully');
    }
    setEditingPlan(null);
    fetchPlans();
  };

  const handleEdit = (plan: WorkoutPlan) => {
    setEditingPlan(plan);
    setModalOpen(true);
  };

  const handleDelete = (plan: WorkoutPlan) => {
    setDeletingPlan(plan);
    setDeleteModalOpen(true);
  };

  const confirmDelete = async () => {
    if (!deletingPlan) return;
    setIsDeleting(true);
    try {
      await workoutPlansApi.delete(deletingPlan.id);
      toast.success('Deleted successfully');
      setDeleteModalOpen(false);
      setDeletingPlan(null);
      fetchPlans();
    } finally {
      setIsDeleting(false);
    }
  };

  const confirmDeleteAll = async () => {
    setIsDeleting(true);
    try {
      await workoutPlansApi.deleteAll();
      toast.success('All plans deleted successfully');
      setDeleteAllModalOpen(false);
      fetchPlans();
    } finally {
      setIsDeleting(false);
    }
  };

  const handleOpenCreate = () => {
    setEditingPlan(null);
    setModalOpen(true);
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-20">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-foreground">Workout Plans</h1>
          <p className="text-muted-foreground mt-1">
            Manage your workout plans and track your progress
          </p>
        </div>
        <div className="flex gap-2">
          {plans.length > 0 && (
            <Button
              variant="outline"
              onClick={() => setDeleteAllModalOpen(true)}
              className="gap-2 text-destructive hover:text-destructive hover:bg-destructive/10"
            >
              <Trash2 className="h-4 w-4" />
              Delete All
            </Button>
          )}
          <Button onClick={handleOpenCreate} className="gap-2">
            <Plus className="h-4 w-4" />
            New Plan
          </Button>
        </div>
      </div>

      {plans.length === 0 ? (
        <div className="flex flex-col items-center justify-center py-20 text-center">
          <div className="rounded-full bg-muted p-6 mb-4">
            <Dumbbell className="h-12 w-12 text-muted-foreground" />
          </div>
          <h3 className="text-xl font-semibold text-foreground mb-2">No workout plans yet</h3>
          <p className="text-muted-foreground mb-6 max-w-md">
            Get started by creating your first workout plan to organize your sessions and track your fitness journey.
          </p>
          <Button onClick={handleOpenCreate} className="gap-2">
            <Plus className="h-4 w-4" />
            Create Your First Plan
          </Button>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {plans.map((plan) => (
            <WorkoutPlanCard
              key={plan.id}
              plan={plan}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          ))}
        </div>
      )}

      <WorkoutPlanModal
        open={modalOpen}
        onOpenChange={setModalOpen}
        plan={editingPlan}
        onSubmit={handleCreateOrUpdate}
      />

      <DeleteConfirmModal
        open={deleteModalOpen}
        onOpenChange={setDeleteModalOpen}
        title="Delete Workout Plan"
        description={`Are you sure you want to delete "${deletingPlan?.name}"? This action cannot be undone and all associated sessions will be removed.`}
        onConfirm={confirmDelete}
        isLoading={isDeleting}
      />

      <DeleteConfirmModal
        open={deleteAllModalOpen}
        onOpenChange={setDeleteAllModalOpen}
        title="Delete All Workout Plans"
        description="Are you sure you want to delete all workout plans? This action cannot be undone and all associated sessions will be removed."
        onConfirm={confirmDeleteAll}
        isLoading={isDeleting}
      />
    </div>
  );
}
